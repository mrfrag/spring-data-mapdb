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

import java.lang.reflect.InvocationTargetException;

import org.apache.commons.beanutils.PropertyUtils;

import com.google.common.base.Predicate;

public class PropertyPredicate<V> implements Predicate<V> {

	private final String property;

	private final Predicate<V> predicate;

	private PropertyPredicate(String property, Predicate<V> predicate) {
		this.property = property;
		this.predicate = predicate;
	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean apply(V input) {
		try {
			return predicate.apply((V) PropertyUtils.getProperty(input, property));
		} catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
			return false;
		}
	}

	public static <V> Predicate<V> wrap(Predicate<V> predicate, String property) {
		return new PropertyPredicate<>(property, predicate);
	}

}
