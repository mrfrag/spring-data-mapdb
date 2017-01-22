/*
 * Copyright 2014-2017 the original author or authors.
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
package org.springframework.data.mapdb.query.predicates;

import com.google.common.base.Predicate;

/**
 * Compares value with provided input as {@link Comparable}, tolerant to
 * {@link Number} types.
 * 
 * @author spv
 *
 */
public class GreaterLessPredicate implements Predicate<Object> {

	private final boolean less;

	private final boolean equal;

	private final Comparable<?> value;

	private GreaterLessPredicate(boolean less, boolean equal, Comparable<?> value) {
		this.less = less;
		this.equal = equal;
		this.value = value;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public boolean apply(Object input) {
		if (input == null || !Comparable.class.isInstance(input)) {
			return false;
		}
		if (!value.getClass().isInstance(input) && !(value instanceof Number)) {
			return false;
		}
		Object inputToCompare = input;
		Object valueToCompare = value;
		if (value instanceof Number) {
			inputToCompare = ((Number) input).doubleValue();
			valueToCompare = ((Number) value).doubleValue();
		}
		int result = ((Comparable) inputToCompare).compareTo(valueToCompare);
		return equal && result == 0 || (less ? (result < 0) : (result > 0));
	}

	/**
	 * Less of Equal
	 * 
	 * @param value
	 * @return {@link GreaterLessPredicate}
	 */
	public static GreaterLessPredicate le(Comparable<?> value) {
		return new GreaterLessPredicate(true, true, value);
	}

	/**
	 * Less
	 * 
	 * @param value
	 * @return {@link GreaterLessPredicate}
	 */
	public static GreaterLessPredicate ls(Comparable<?> value) {
		return new GreaterLessPredicate(true, false, value);
	}

	/**
	 * Greater of Equal
	 * 
	 * @param value
	 * @return {@link GreaterLessPredicate}
	 */
	public static GreaterLessPredicate ge(Comparable<?> value) {
		return new GreaterLessPredicate(false, true, value);
	}

	/**
	 * Greater
	 * 
	 * @param value
	 * @return {@link GreaterLessPredicate}
	 */
	public static GreaterLessPredicate gr(Comparable<?> value) {
		return new GreaterLessPredicate(false, false, value);
	}

}
