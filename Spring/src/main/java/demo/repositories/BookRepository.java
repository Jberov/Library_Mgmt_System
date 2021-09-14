package demo.repositories;

import demo.entities.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface BookRepository extends JpaRepository<Book, String> {
	Book findByName(String name);
	
	List<Book> findByExistence(boolean status);
	
	Book findByIsbn(String isbn);
}

