package demo.repositories;

import demo.entities.Books;
import org.springframework.data.jpa.repository.JpaRepository;


import org.springframework.stereotype.Repository;

import java.util.LinkedList;


@Repository
public interface BookRepository extends JpaRepository<Books, String> {

    Books findByName(String name);
    LinkedList<Books> findByExistence(boolean status);
    Books findByIsbn(String isbn);

}

