/*
 * Copyright 2017-2018 the original author or authors.
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
package org.springframework.data.mapdb;

import java.io.Serializable;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.keyvalue.core.QueryEngine;
import org.springframework.data.mapdb.query.MapDbCriteriaAccessor;
import org.springframework.data.mapdb.query.MapDbSortAccessor;

import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;
import com.google.common.collect.Lists;

public class MapDbQueryEngine extends QueryEngine<MapDbKeyValueAdapter, Predicate<?>, Comparator<?>> {

	public MapDbQueryEngine() {
		super(new MapDbCriteriaAccessor(), new MapDbSortAccessor());
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public Collection<?> execute(Predicate<?> criteria, Comparator<?> sort, int offset, int rows, Serializable keyspace) {
		Collection<Object> values = getAdapter().getMap(keyspace).values();
		Collection<?> result = Lists.newArrayList(criteria != null ? Collections2.filter(values, (Predicate<Object>) criteria) : values);
		if (sort != null) {
			List<?> listResult = (List<?>) Lists.newArrayList(result)
												.parallelStream()
												.sorted((Comparator) sort)
												.collect(Collectors.toList());
			return offset != rows ? listResult.subList(offset, offset + rows) : listResult;
		} else {
			return offset != rows ? Lists.newArrayList(result).subList(offset, offset + rows) : result;
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public long count(Predicate<?> criteria, Serializable keyspace) {
		return Collections2.filter(getAdapter().getMap(keyspace).values(), (Predicate<Object>) criteria).size();
	}

}
