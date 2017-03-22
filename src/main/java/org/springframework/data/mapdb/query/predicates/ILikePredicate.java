package org.springframework.data.mapdb.query.predicates;

import java.util.regex.Pattern;

public class ILikePredicate extends LikePredicate {

	public ILikePredicate(String expression) {
		super(expression);
	}

	@Override
	protected int getFlags() {
		return Pattern.CASE_INSENSITIVE;
	}

}
