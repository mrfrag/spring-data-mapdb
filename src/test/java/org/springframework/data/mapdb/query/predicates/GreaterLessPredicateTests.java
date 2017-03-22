package org.springframework.data.mapdb.query.predicates;

import java.time.LocalDate;
import java.util.function.Predicate;

import org.junit.Assert;
import org.junit.Test;

public class GreaterLessPredicateTests {

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Test
	public void testLess() {
		Predicate predicate = GreaterLessPredicate.ls(10l);

		Assert.assertTrue(predicate.test(5));
		Assert.assertFalse(predicate.test(10));
		Assert.assertFalse(predicate.test(15));

		Assert.assertTrue(predicate.test(new Integer(8)));
		Assert.assertFalse(predicate.test(new Integer(12)));
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Test
	public void testGreater() {
		Predicate predicate = GreaterLessPredicate.gr(10);

		Assert.assertTrue(predicate.test(15));
		Assert.assertFalse(predicate.test(10));
		Assert.assertFalse(predicate.test(5));

		Assert.assertTrue(predicate.test(new Integer(18)));
		Assert.assertFalse(predicate.test(new Integer(2)));
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Test
	public void testLessOrEqual() {
		Predicate predicate = GreaterLessPredicate.le(new Long(10));

		Assert.assertTrue(predicate.test(5l));
		Assert.assertTrue(predicate.test(10l));
		Assert.assertFalse(predicate.test(15l));

		Assert.assertTrue(predicate.test(new Long(8)));
		Assert.assertTrue(predicate.test(new Long(10)));
		Assert.assertFalse(predicate.test(new Long(12)));
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Test
	public void testGreaterOrEqual() {
		Predicate predicate = GreaterLessPredicate.ge(10);

		Assert.assertTrue(predicate.test(15));
		Assert.assertTrue(predicate.test(10));
		Assert.assertFalse(predicate.test(5));

		Assert.assertTrue(predicate.test(new Integer(18)));
		Assert.assertTrue(predicate.test(new Integer(10)));
		Assert.assertFalse(predicate.test(new Integer(2)));
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Test
	public void testGreaterOrEqualDate() {
		Predicate predicate = GreaterLessPredicate.ge(LocalDate.now());

		Assert.assertTrue(predicate.test(LocalDate.now().plusYears(10)));
		Assert.assertFalse(predicate.test(LocalDate.now().minusYears(10)));

	}

}
