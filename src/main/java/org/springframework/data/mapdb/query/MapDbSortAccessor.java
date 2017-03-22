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
