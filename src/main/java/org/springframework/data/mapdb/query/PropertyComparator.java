package org.springframework.data.mapdb.query;

import java.io.Serializable;
import java.util.Comparator;

import org.apache.commons.beanutils.PropertyUtils;

public class PropertyComparator implements Comparator<Object>, Serializable {

	private static final long serialVersionUID = 1L;

	private String attributeName;
	private int direction;

	public PropertyComparator(String attributeName, boolean ascending) {
		this.attributeName = attributeName;
		this.direction = (ascending ? 1 : -1);
	}

	/**
	 *
	 * @param o1
	 *            An entry in a map
	 * @param o2
	 *            Another entry in the map
	 * @return Comparison result
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public int compare(Object o1, Object o2) {

		try {
			Object o1Field = PropertyUtils.getProperty(o1, this.attributeName);
			Object o2Field = PropertyUtils.getProperty(o2, this.attributeName);

			if (o1Field == null) {
				return this.direction;
			}
			if (o2Field == null) {
				return -1 * this.direction;
			}
			if (o1Field instanceof Comparable && o2Field instanceof Comparable) {
				return this.direction * ((Comparable) o1Field).compareTo((Comparable) o2Field);
			}

		} catch (Exception ignore) {
			return 0;
		}

		return 0;
	}
}
