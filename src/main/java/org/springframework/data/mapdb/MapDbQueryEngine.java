package org.springframework.data.mapdb;

import java.io.Serializable;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.springframework.data.keyvalue.core.QueryEngine;
import org.springframework.data.mapdb.query.MapDbCriteriaAccessor;
import org.springframework.data.mapdb.query.MapDbSortAccessor;

public class MapDbQueryEngine extends QueryEngine<MapDbKeyValueAdapter, Predicate<?>, Comparator<?>> {

	public MapDbQueryEngine() {
		super(new MapDbCriteriaAccessor(), new MapDbSortAccessor());
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public Collection<?> execute(Predicate<?> criteria, Comparator<?> sort, int offset, int rows, Serializable keyspace) {
		Collection<Object> values = getAdapter().getMap(keyspace).values();

		List<?> result = values	.parallelStream()
								.filter(input -> criteria != null ? ((Predicate) criteria).test(input) : true)
								.sorted((o1, o2) -> sort != null ? ((Comparator) sort).compare(o1, o2) : 0)
								.collect(Collectors.toList());

		return offset != rows ? result.subList(offset, offset + rows) : result;
	}

	@SuppressWarnings({ "unchecked" })
	@Override
	public long count(Predicate<?> criteria, Serializable keyspace) {
		if (criteria == null) {
			return getAdapter().getMap(keyspace).getSize();
		}
		return getAdapter().getMap(keyspace).values().parallelStream().filter(criteria).count();
	}

}
