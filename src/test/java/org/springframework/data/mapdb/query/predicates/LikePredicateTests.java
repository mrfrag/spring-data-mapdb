package org.springframework.data.mapdb.query.predicates;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.function.Predicate;

import org.junit.Assert;
import org.junit.Test;

public class LikePredicateTests {

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Test
	public void testLike() {
		Predicate predicatre = new LikePredicate("%some%");

		assertTrue(predicatre.test("this is some text"));
		assertFalse(predicatre.test("this is another text"));
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Test
	public void testLikeCaseSensitive() {
		Predicate predicatre = new LikePredicate("%some%");

		assertFalse(predicatre.test("this is Some text"));
	}

}
