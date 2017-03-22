package org.springframework.data.mapdb.query.predicates;

import java.util.function.Predicate;

import org.junit.Assert;
import org.junit.Test;

public class LikePredicateTests {

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Test
	public void testLike() {
		Predicate predicatre = new LikePredicate("%some%");

		Assert.assertTrue(predicatre.test("this is some text"));
		Assert.assertFalse(predicatre.test("this is another text"));
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Test
	public void testLikeCaseSensitive() {
		Predicate predicatre = new LikePredicate("%some%");

		Assert.assertFalse(predicatre.test("this is Some text"));
	}

}
