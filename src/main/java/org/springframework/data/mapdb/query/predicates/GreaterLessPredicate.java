package org.springframework.data.mapdb.query.predicates;

import java.util.function.Predicate;

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
	public boolean test(Object input) {
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
		return equal && result == 0 || (less ? result < 0 : result > 0);
	}

	/**
	 * Less or equal
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
	 * Greater or equal
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
