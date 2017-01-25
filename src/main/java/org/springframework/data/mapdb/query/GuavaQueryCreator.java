/*
 * Copyright 2017-2018 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.springframework.data.mapdb.query;

import java.util.Iterator;

import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.data.domain.Sort;
import org.springframework.data.keyvalue.core.query.KeyValueQuery;
import org.springframework.data.mapdb.query.predicates.GreaterLessPredicate;
import org.springframework.data.mapdb.query.predicates.ILikePredicate;
import org.springframework.data.mapdb.query.predicates.LikePredicate;
import org.springframework.data.mapdb.query.predicates.PropertyPredicate;
import org.springframework.data.repository.query.ParameterAccessor;
import org.springframework.data.repository.query.parser.AbstractQueryCreator;
import org.springframework.data.repository.query.parser.Part;
import org.springframework.data.repository.query.parser.Part.IgnoreCaseType;
import org.springframework.data.repository.query.parser.Part.Type;
import org.springframework.data.repository.query.parser.PartTree;

import com.google.common.base.Predicate;
import com.google.common.base.Predicates;

public class GuavaQueryCreator extends AbstractQueryCreator<KeyValueQuery<Predicate<?>>, Predicate<?>> {

	public GuavaQueryCreator(PartTree tree) {
		super(tree);
	}

	public GuavaQueryCreator(PartTree tree, ParameterAccessor parameters) {
		super(tree, parameters);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.springframework.data.repository.query.parser.AbstractQueryCreator
	 * #create(org.springframework.data.repository.query.parser.Part,
	 * java.util.Iterator)
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	protected Predicate<?> create(Part part, Iterator<Object> iterator) {
		return this.from(part, (Iterator<Comparable<?>>) (Iterator) iterator);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.springframework.data.repository.query.parser.AbstractQueryCreator
	 * #and(org.springframework.data.repository.query.parser.Part,
	 * java.lang.Object, java.util.Iterator)
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	protected Predicate<?> and(Part part, Predicate<?> base, Iterator<Object> iterator) {
		return Predicates.and((Predicate<Object>) base, (Predicate<Object>) this.from(part, (Iterator<Comparable<?>>) (Iterator) iterator));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.springframework.data.repository.query.parser.AbstractQueryCreator
	 * #or(java.lang.Object, java.lang.Object)
	 */
	@SuppressWarnings("unchecked")
	@Override
	protected Predicate<?> or(Predicate<?> base, Predicate<?> criteria) {
		return Predicates.or((Predicate<Object>) base, (Predicate<Object>) criteria);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.springframework.data.repository.query.parser.AbstractQueryCreator
	 * #complete(java.lang.Object, org.springframework.data.domain.Sort)
	 */
	@Override
	protected KeyValueQuery<Predicate<?>> complete(Predicate<?> criteria, Sort sort) {

		KeyValueQuery<Predicate<?>> keyValueQuery = new KeyValueQuery<Predicate<?>>(criteria);

		if (sort != null) {
			keyValueQuery.setSort(sort);
		}
		return keyValueQuery;
	}

	private Predicate<?> from(Part part, Iterator<Comparable<?>> iterator) {

		String property = part.getProperty().toDotPath();
		Type type = part.getType();
		boolean ignoreCase = (part.shouldIgnoreCase() != IgnoreCaseType.NEVER);

		Predicate<?> predicate = null;

		switch (type) {

		case FALSE:
		case TRUE:
			predicate = fromBooleanVariant(type);
			break;

		case SIMPLE_PROPERTY:
			predicate = fromEqualityVariant(type, ignoreCase, iterator);
			break;

		case GREATER_THAN:
		case GREATER_THAN_EQUAL:
		case LESS_THAN:
		case LESS_THAN_EQUAL:
			predicate = fromInequalityVariant(type, ignoreCase, iterator);
			break;

		case LIKE:
			predicate = fromLikeVariant(type, iterator);
			break;

		case IS_NOT_NULL:
		case IS_NULL:
			predicate = fromNullVariant(type);
			break;

		/*
		 * case AFTER: case BEFORE: case BETWEEN: case CONTAINING: case
		 * ENDING_WITH: case EXISTS: case IN: case NEAR: case
		 * NEGATING_SIMPLE_PROPERTY: case NOT_CONTAINING: case NOT_IN: case
		 * NOT_LIKE: case REGEX: case STARTING_WITH: case WITHIN:
		 */
		default:
			throw new InvalidDataAccessApiUsageException(String.format("Found invalid part '%s' in query", type));
		}

		return PropertyPredicate.wrap(predicate, property);

	}

	private Predicate<?> fromBooleanVariant(Type type) {

		switch (type) {

		case TRUE:
			return Predicates.equalTo(true);
		case FALSE:
			return Predicates.equalTo(false);

		default:
			throw new InvalidDataAccessApiUsageException(String.format("Logic error for '%s' in query", type));
		}
	}

	private Predicate<?> fromInequalityVariant(Type type, boolean ignoreCase, Iterator<Comparable<?>> iterator) {

		if (ignoreCase && type != Type.SIMPLE_PROPERTY) {
			throw new InvalidDataAccessApiUsageException(String.format("Ignore case not supported for '%s'", type));
		}

		switch (type) {

		case GREATER_THAN:
			return GreaterLessPredicate.gr(iterator.next());
		case GREATER_THAN_EQUAL:
			return GreaterLessPredicate.ge(iterator.next());
		case LESS_THAN:
			return GreaterLessPredicate.ls(iterator.next());
		case LESS_THAN_EQUAL:
			return GreaterLessPredicate.le(iterator.next());

		default:
			throw new InvalidDataAccessApiUsageException(String.format("Logic error for '%s' in query", type));
		}
	}

	private Predicate<?> fromEqualityVariant(Type type, boolean ignoreCase, Iterator<Comparable<?>> iterator) {

		switch (type) {

		case SIMPLE_PROPERTY:
			if (ignoreCase) {
				return new ILikePredicate(iterator.next().toString());
			} else {
				return Predicates.equalTo(iterator.next());
			}

		default:
			throw new InvalidDataAccessApiUsageException(String.format("Logic error for '%s' in query", type));
		}
	}

	private Predicate<?> fromLikeVariant(Type type, Iterator<Comparable<?>> iterator) {

		switch (type) {

		case LIKE:
			return new LikePredicate(iterator.next().toString());

		default:
			throw new InvalidDataAccessApiUsageException(String.format("Logic error for '%s' in query", type));
		}
	}

	private Predicate<?> fromNullVariant(Type type) {

		switch (type) {

		case IS_NULL:
			return Predicates.isNull();
		case IS_NOT_NULL:
			return Predicates.notNull();

		default:
			throw new InvalidDataAccessApiUsageException(String.format("Logic error for '%s' in query", type));
		}
	}

}
