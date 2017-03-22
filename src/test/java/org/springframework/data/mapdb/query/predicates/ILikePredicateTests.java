package org.springframework.data.mapdb.query.predicates;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.function.Predicate;

import org.junit.Test;

public class ILikePredicateTests {

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Test
	public void testLike() {
		Predicate predicatre = new ILikePredicate("%some%");

		assertTrue(predicatre.test("this is some text"));
		assertFalse(predicatre.test("this is another text"));
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Test
	public void testLikeCaseSensitive() {
		Predicate predicatre = new ILikePredicate("%some%");

		assertTrue(predicatre.test("this is Some text"));
		assertTrue(predicatre.test("this is some text"));
	}

}
