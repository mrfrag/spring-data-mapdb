package org.springframework.data.mapdb.query.predicates;

import java.util.function.Predicate;

public class EqualsPredicate implements Predicate<Object> {

	private final Object expected;

	public EqualsPredicate(Object expected) {
		this.expected = expected;
	}

	@Override
	public boolean test(Object input) {
		return expected.equals(input);
	}

}
