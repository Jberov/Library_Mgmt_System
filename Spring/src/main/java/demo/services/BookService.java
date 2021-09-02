package demo.services;

import demo.dao.BookRecordsDAO;
import demo.dao.BooksDAOImpl;
import demo.dto.BookDTO;
import demo.entities.Books;
import demo.mappers.BookMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.LinkedList;

@Service
public class BookService {

    @Autowired
    private BooksDAOImpl booksDAO;

    @Autowired
    private BookRecordsDAO bookRecordsDAO;

    @Autowired
    private BookMapper bookMapper;

    public LinkedList<BookDTO> getAllBooks () {
        return bookMapper.linkedListToDTO(booksDAO.getAllBooks());
    }

    public String addBookAdmin (String isbn, int count, String name, String author, String description) {
        if (booksDAO.getBook(isbn) == null) {
            return booksDAO.addBookAdmin(isbn, count, name, author, description);
        } else {
            if (!booksDAO.getBook(isbn).getIsbn().equals(name)) {
                return "Wrong isbn! Book with such isbn already exists";
            }
            booksDAO.increaseCount(isbn,count);
            return "Such book already exists. Count increased with amount specified in the request";
        }
    }

    public String deleteBook (String isbn) {
        if (!booksDAO.bookExistsByID(isbn)) {
            return "No such book found";
        } else if (bookRecordsDAO.userHistoryExists(isbn)) {
            return "Not all users have returned this book yet. Please, acquire all copies before removing it from the library";
        }
        booksDAO.deleteBookAdmin(isbn);
        return "Book successfully deleted";

    }

    public BookDTO getBookById (String isbn) {
        return bookMapper.bookToDTO(booksDAO.getBook(isbn));
    }
}

