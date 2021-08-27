package demo.dto;

import demo.dao.BookRecordsDAO;
import demo.dao.BooksDAOImpl;
import demo.entities.Books;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.LinkedList;

@Service
public class BookDTO {

    @Autowired
    private BooksDAOImpl booksDAO;
    @Autowired
    private BookRecordsDAO bookRecordsDAO;

    public LinkedList<Books> getAllBooks(){
        return booksDAO.getAllBooks();
    }

    public String addBookAdmin(String isbn, int count, String name, String author, String description){
        if (booksDAO.getBook(isbn) == null) {
            return booksDAO.addBookAdmin(isbn, count, name, author, description);
        } else {
            if(!booksDAO.getBook(isbn).getIsbn().equals(name)){
                return "Wrong isbn! Book with such isbn already exists";
            }else{
                booksDAO.increaseCount(isbn,count);
                return "Such book already exists. Count increased with amount specified in the request";
            }
        }
    }
    public ResponseEntity<String> deleteBook(String isbn){
        if(!booksDAO.bookExistsByID(isbn)){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No such book found");
        }else if(bookRecordsDAO.userHistoryExists(isbn)){
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Not all users have returned this book yet. Please, acquire all copies before removing it from the library");
        }else{
            booksDAO.deleteBookAdmin(isbn);
            return ResponseEntity.status(HttpStatus.OK).body("Book successfully deleted");
        }
    }

    public Books getBookById(String isbn){
        if(booksDAO.bookExistsByID(isbn)){
            return booksDAO.getBook(isbn);
        }else{
            return null;
        }
    }
}

