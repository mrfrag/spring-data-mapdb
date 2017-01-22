package org.springframework.data.mapdb.query.predicates;

import org.junit.Assert;
import org.junit.Test;

import com.google.common.base.Predicate;

public class ILikePredicateTests {

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Test
	public void testLike() {
		Predicate predicatre = new ILikePredicate("%some%");

		Assert.assertTrue(predicatre.apply("this is some text"));
		Assert.assertFalse(predicatre.apply("this is another text"));
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Test
	public void testLikeCaseSensitive() {
		Predicate predicatre = new ILikePredicate("%some%");

		Assert.assertTrue(predicatre.apply("this is Some text"));
		Assert.assertTrue(predicatre.apply("this is some text"));
	}

}
