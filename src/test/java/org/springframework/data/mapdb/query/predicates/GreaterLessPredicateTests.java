package org.springframework.data.mapdb.query.predicates;

import java.time.LocalDate;

import org.junit.Assert;
import org.junit.Test;

import com.google.common.base.Predicate;

public class GreaterLessPredicateTests {

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Test
	public void testLess() {
		Predicate predicate = GreaterLessPredicate.ls(10l);

		Assert.assertTrue(predicate.apply(5));
		Assert.assertFalse(predicate.apply(10));
		Assert.assertFalse(predicate.apply(15));

		Assert.assertTrue(predicate.apply(new Integer(8)));
		Assert.assertFalse(predicate.apply(new Integer(12)));
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Test
	public void testGreater() {
		Predicate predicate = GreaterLessPredicate.gr(10);

		Assert.assertTrue(predicate.apply(15));
		Assert.assertFalse(predicate.apply(10));
		Assert.assertFalse(predicate.apply(5));

		Assert.assertTrue(predicate.apply(new Integer(18)));
		Assert.assertFalse(predicate.apply(new Integer(2)));
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Test
	public void testLessOrEqual() {
		Predicate predicate = GreaterLessPredicate.le(new Long(10));

		Assert.assertTrue(predicate.apply(5l));
		Assert.assertTrue(predicate.apply(10l));
		Assert.assertFalse(predicate.apply(15l));

		Assert.assertTrue(predicate.apply(new Long(8)));
		Assert.assertTrue(predicate.apply(new Long(10)));
		Assert.assertFalse(predicate.apply(new Long(12)));
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Test
	public void testGreaterOrEqual() {
		Predicate predicate = GreaterLessPredicate.ge(10);

		Assert.assertTrue(predicate.apply(15));
		Assert.assertTrue(predicate.apply(10));
		Assert.assertFalse(predicate.apply(5));

		Assert.assertTrue(predicate.apply(new Integer(18)));
		Assert.assertTrue(predicate.apply(new Integer(10)));
		Assert.assertFalse(predicate.apply(new Integer(2)));
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Test
	public void testGreaterOrEqualDate() {
		Predicate predicate = GreaterLessPredicate.ge(LocalDate.now());

		Assert.assertTrue(predicate.apply(LocalDate.now().plusYears(10)));
		Assert.assertFalse(predicate.apply(LocalDate.now().minusYears(10)));

	}

}
