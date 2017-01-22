package org.springframework.data.mapdb.query.predicates;

import org.junit.Assert;
import org.junit.Test;

import com.google.common.base.Predicate;

public class LikePredicateTests {

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Test
	public void testLike() {
		Predicate predicatre = new LikePredicate("%some%");

		Assert.assertTrue(predicatre.apply("this is some text"));
		Assert.assertFalse(predicatre.apply("this is another text"));
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Test
	public void testLikeCaseSensitive() {
		Predicate predicatre = new LikePredicate("%some%");

		Assert.assertFalse(predicatre.apply("this is Some text"));
	}

}
