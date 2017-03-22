package org.springframework.data.mapdb.query;

import java.util.function.Predicate;

import org.springframework.data.keyvalue.core.CriteriaAccessor;
import org.springframework.data.keyvalue.core.query.KeyValueQuery;

public class MapDbCriteriaAccessor implements CriteriaAccessor<Predicate<?>> {

	@Override
	public Predicate<?> resolve(KeyValueQuery<?> query) {

		if (query == null || query.getCritieria() == null) {
			return null;
		}

		if (query.getCritieria() instanceof Predicate) {
			return (Predicate<?>) query.getCritieria();
		}

		throw new IllegalArgumentException("Cannot create criteria for " + query.getCritieria());
	}

}
