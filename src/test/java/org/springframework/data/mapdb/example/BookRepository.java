package org.springframework.data.mapdb.example;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.data.mapdb.repository.MapDbRepository;

public interface BookRepository extends MapDbRepository<Book, String> {

	List<Book> findByTitle(String title);

	List<Book> findByGenre(String genre, Sort sort);

	List<Book> findByGenreAndPages(String genre, int pages);

}
