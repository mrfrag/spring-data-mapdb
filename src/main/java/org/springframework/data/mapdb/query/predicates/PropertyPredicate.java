package org.springframework.data.mapdb.query.predicates;

import java.lang.reflect.InvocationTargetException;
import java.util.function.Predicate;

import org.apache.commons.beanutils.PropertyUtils;

public class PropertyPredicate<V> implements Predicate<V> {

	private final String property;

	private final Predicate<V> predicate;

	private PropertyPredicate(String property, Predicate<V> predicate) {
		this.property = property;
		this.predicate = predicate;
	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean test(V input) {
		try {
			return predicate.test((V) PropertyUtils.getProperty(input, property));
		} catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
			return false;
		}
	}

	public static <V> Predicate<V> wrap(Predicate<V> predicate, String property) {
		return new PropertyPredicate<>(property, predicate);
	}

}
