package org.springframework.data.mapdb.query.predicates;

import java.util.function.Predicate;
import java.util.regex.Pattern;

public class LikePredicate implements Predicate<Object> {

	private final String expression;

	public LikePredicate(String expression) {
		this.expression = expression;
	}

	@Override
	public boolean test(Object input) {
		if (input == null) {
			return expression == null;
		} else if (!String.class.isInstance(input)) {
			return false;
		} else {
			// we quote the input string then escape then replace % and _
			// at the end we have a regex pattern look like :
			// \QSOME_STRING\E.*\QSOME_OTHER_STRING\E
			String regex = Pattern	.quote(expression)
									// escaped %
									.replaceAll("(?<!\\\\)[%]", "\\\\E.*\\\\Q")
									// escaped _
									.replaceAll("(?<!\\\\)[_]", "\\\\E.\\\\Q")
									// non escaped %
									.replaceAll("\\\\%", "%")
									// non escaped _
									.replaceAll("\\\\_", "_");
			return Pattern.compile(regex, getFlags()).matcher((String) input).matches();
		}
	}

	protected int getFlags() {
		return 0;
	}

}
