package org.springframework.data.mapdb.example;

import java.time.LocalDate;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { BookExampleConfiguration.class })
@DirtiesContext
public class BookMapDbExampleTests {

	@Autowired
	private BookRepository bookRepository;

	@Test
	public void testExample() {
		Writer author = new Writer();
		author.setName("Sam The Author");
		author.setBirthdate(LocalDate.now().minusYears(30));

		Book book = new Book();
		book.setIsbn("1234-12341234");
		book.setTitle("The Book");
		book.setPages(300);
		book.setGenre("fantasy");
		book.setPublicationDate(LocalDate.now());
		book.setAuthor(author);

		bookRepository.save(book);

		Assert.assertEquals(book, bookRepository.findOne(book.getIsbn()));

		List<Book> books = bookRepository.findByTitle(book.getTitle());
		Assert.assertTrue(CollectionUtils.isNotEmpty(books));
		Assert.assertEquals(book, books.get(0));

		books = bookRepository.findByGenre(book.getGenre(), new Sort(Direction.ASC, "pages"));
		Assert.assertTrue(CollectionUtils.isNotEmpty(books));
		Assert.assertEquals(book, books.get(0));

		Iterable<Book> iBooks = bookRepository.findAll(new Sort(Direction.ASC, "pages"));
		Assert.assertTrue(iBooks.iterator().hasNext());
		Assert.assertEquals(book, books.iterator().next());

		books = bookRepository.findByGenreAndPages(book.getGenre(), book.getPages());
		Assert.assertTrue(CollectionUtils.isNotEmpty(books));
		Assert.assertEquals(book, books.get(0));
	}

}
