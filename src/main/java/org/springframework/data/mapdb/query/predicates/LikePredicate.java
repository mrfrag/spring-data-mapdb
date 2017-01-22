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

import java.util.regex.Pattern;

import com.google.common.base.Predicate;

public class LikePredicate implements Predicate<Object> {

	private final String expression;

	public LikePredicate(String expression) {
		this.expression = expression;
	}

	@Override
	public boolean apply(Object input) {
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
