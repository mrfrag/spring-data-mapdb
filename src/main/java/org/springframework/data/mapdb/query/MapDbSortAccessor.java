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
package org.springframework.data.mapdb.query;

import java.util.Comparator;

import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.NullHandling;
import org.springframework.data.domain.Sort.Order;
import org.springframework.data.keyvalue.core.SortAccessor;
import org.springframework.data.keyvalue.core.query.KeyValueQuery;
import org.springframework.util.comparator.CompoundComparator;

public class MapDbSortAccessor implements SortAccessor<Comparator<?>> {

	/**
	 * Sort on a sequence of fields, possibly none.
	 *
	 * @param query
	 *            If not null, will contain one of more {@link Sort.Order}
	 *            objects.
	 * @return A sequence of comparators or {@code null}
	 */
	public Comparator<?> resolve(KeyValueQuery<?> query) {

		if (query == null || query.getSort() == null) {
			return null;
		}

		CompoundComparator<Object> compoundComparator = new CompoundComparator<>();

		for (Order order : query.getSort()) {

			if (order.getProperty().indexOf('.') > -1) {
				throw new UnsupportedOperationException("Embedded fields not implemented: " + order);
			}

			if (order.isIgnoreCase()) {
				throw new UnsupportedOperationException("Ignore case not implemented: " + order);
			}

			if (NullHandling.NATIVE != order.getNullHandling()) {
				throw new UnsupportedOperationException("Null handling not implemented: " + order);
			}

			PropertyComparator propertyComparator = new PropertyComparator(order.getProperty(),
					order.isAscending());

			compoundComparator.addComparator(propertyComparator);
		}

		return compoundComparator;
	}

}
