package Library.demo.dao;

import Library.demo.entities.Books;
import org.hibernate.NonUniqueObjectException;
import org.hibernate.exception.JDBCConnectionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.InputMismatchException;
import java.util.LinkedList;

@Service
public class BooksDAOImpl {
    @Autowired
    BookRepository bookRepository;

    public LinkedList<Books> get_all_books() {
        LinkedList<Books> books = new LinkedList<>();
            for(Books book : bookRepository.findAll()) {
                books.add(book);
                System.out.println(book);
            }
        return books;
    }
    public void addBook_admin(int count, String name, String author, String description) throws InputMismatchException, JDBCConnectionException, NonUniqueObjectException {
        Books book = new Books(count,name, author,description);
        bookRepository.save(book);
    }

}
