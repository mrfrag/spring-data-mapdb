package org.springframework.data.mapdb.query.predicates;

import java.util.function.Predicate;

public class NullCheckPredicate implements Predicate<Object> {

	private final boolean notNull;

	private NullCheckPredicate(boolean notNull) {
		this.notNull = notNull;
	}

	@Override
	public boolean test(Object value) {
		return notNull ? value != null : value == null;
	}

	public static NullCheckPredicate notNull() {
		return new NullCheckPredicate(true);
	}

	public static NullCheckPredicate isNull() {
		return new NullCheckPredicate(false);
	}

}
