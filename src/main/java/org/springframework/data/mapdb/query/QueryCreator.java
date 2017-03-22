package org.springframework.data.mapdb.query;

import java.util.Iterator;
import java.util.function.Predicate;

import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.data.domain.Sort;
import org.springframework.data.keyvalue.core.query.KeyValueQuery;
import org.springframework.data.mapdb.query.predicates.EqualsPredicate;
import org.springframework.data.mapdb.query.predicates.GreaterLessPredicate;
import org.springframework.data.mapdb.query.predicates.ILikePredicate;
import org.springframework.data.mapdb.query.predicates.LikePredicate;
import org.springframework.data.mapdb.query.predicates.NullCheckPredicate;
import org.springframework.data.mapdb.query.predicates.PropertyPredicate;
import org.springframework.data.repository.query.ParameterAccessor;
import org.springframework.data.repository.query.parser.AbstractQueryCreator;
import org.springframework.data.repository.query.parser.Part;
import org.springframework.data.repository.query.parser.Part.IgnoreCaseType;
import org.springframework.data.repository.query.parser.Part.Type;
import org.springframework.data.repository.query.parser.PartTree;

public class QueryCreator extends AbstractQueryCreator<KeyValueQuery<Predicate<?>>, Predicate<?>> {

	public QueryCreator(PartTree tree) {
		super(tree);
	}

	public QueryCreator(PartTree tree, ParameterAccessor parameters) {
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
		return from(part, (Iterator<Comparable<?>>) (Iterator) iterator);
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
		return ((Predicate<Object>) base).and((Predicate<Object>) from(part, (Iterator<Comparable<?>>) (Iterator) iterator));
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
		return ((Predicate<Object>) base).or((Predicate<Object>) criteria);
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

		case TRUE:
			predicate = new EqualsPredicate(true);
			break;

		case FALSE:
			predicate = new EqualsPredicate(false);
			break;

		case SIMPLE_PROPERTY:
			if (ignoreCase) {
				predicate = new ILikePredicate(iterator.next().toString());
			} else {
				predicate = new EqualsPredicate(iterator.next());
			}
			break;

		case GREATER_THAN:
		case GREATER_THAN_EQUAL:
		case LESS_THAN:
		case LESS_THAN_EQUAL:
			predicate = fromInequalityVariant(type, ignoreCase, iterator);
			break;

		case LIKE:
			predicate = new LikePredicate(iterator.next().toString());
			break;

		case IS_NULL:
			predicate = NullCheckPredicate.isNull();
			break;

		case IS_NOT_NULL:
			predicate = NullCheckPredicate.notNull();
			break;

		// TODO
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

}
