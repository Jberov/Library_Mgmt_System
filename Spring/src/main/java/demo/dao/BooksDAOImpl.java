package demo.dao;

import demo.entities.Books;
import demo.repositories.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import java.util.LinkedList;

@Service
public class BooksDAOImpl {

    @Autowired
    BookRepository bookRepository;

    public LinkedList<Books> getAllBooks() throws ResponseStatusException {
        return new LinkedList<>(bookRepository.findByExistence(true));
    }

    public Books addBook (Books book) {
        book.setExists(true);
        bookRepository.save(book);
        return bookRepository.findByIsbn(book.getIsbn());
    }

    public Books deleteBookAdmin (String isbn) {
        Books temp = bookRepository.findByIsbn(isbn);
        temp.setExists(false);
        temp.setCount(0);
        bookRepository.save(temp);
        return temp;
    }

    public Books restoreBook(Books book){
        Books temp = bookRepository.findByIsbn(book.getIsbn());
        temp.setExists(true);
        temp.setCount(book.getCount());
        bookRepository.save(temp);
        return temp;
    }

    public Books getBook (String isbn) {
        return bookRepository.findByIsbn(isbn);
    }


    public boolean bookExistsByID (String isbn) {
        if(bookRepository.findByIsbn(isbn) == null){
            return false;
        }
        return bookRepository.findByIsbn(isbn).isExists();
    }


    public void decreaseCount (String bookId) {
        Books temp = bookRepository.findByIsbn(bookId);
        temp.setCount(temp.getCount() - 1);
        bookRepository.save(temp);
    }

    public void increaseCountBy1 (String bookId) {
        Books temp = bookRepository.getById(bookId);
        temp.setCount(temp.getCount() + 1);
        bookRepository.save(temp);
    }
    public Books increaseCount (String bookId, int count) {
        Books temp = bookRepository.getById(bookId);
        temp.setCount(temp.getCount() + count);
        bookRepository.save(temp);
        return temp;
    }

    public int checkCount (String isbn) {
        return bookRepository.findByIsbn(isbn).getCount();
    }
}

