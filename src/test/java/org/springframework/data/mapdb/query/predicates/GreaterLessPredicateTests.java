package org.springframework.data.mapdb.query.predicates;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.time.LocalDate;
import java.util.function.Predicate;

import org.junit.Test;

public class GreaterLessPredicateTests {

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Test
	public void testLess() {
		Predicate predicate = GreaterLessPredicate.ls(10l);

		assertTrue(predicate.test(5));
		assertFalse(predicate.test(10));
		assertFalse(predicate.test(15));

		assertTrue(predicate.test(new Integer(8)));
		assertFalse(predicate.test(new Integer(12)));
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Test
	public void testGreater() {
		Predicate predicate = GreaterLessPredicate.gr(10);

		assertTrue(predicate.test(15));
		assertFalse(predicate.test(10));
		assertFalse(predicate.test(5));

		assertTrue(predicate.test(new Integer(18)));
		assertFalse(predicate.test(new Integer(2)));
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Test
	public void testLessOrEqual() {
		Predicate predicate = GreaterLessPredicate.le(new Long(10));

		assertTrue(predicate.test(5l));
		assertTrue(predicate.test(10l));
		assertFalse(predicate.test(15l));

		assertTrue(predicate.test(new Long(8)));
		assertTrue(predicate.test(new Long(10)));
		assertFalse(predicate.test(new Long(12)));
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Test
	public void testGreaterOrEqual() {
		Predicate predicate = GreaterLessPredicate.ge(10);

		assertTrue(predicate.test(15));
		assertTrue(predicate.test(10));
		assertFalse(predicate.test(5));

		assertTrue(predicate.test(new Integer(18)));
		assertTrue(predicate.test(new Integer(10)));
		assertFalse(predicate.test(new Integer(2)));
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Test
	public void testGreaterOrEqualDate() {
		Predicate predicate = GreaterLessPredicate.ge(LocalDate.now());

		assertTrue(predicate.test(LocalDate.now().plusYears(10)));
		assertFalse(predicate.test(LocalDate.now().minusYears(10)));

	}

}
