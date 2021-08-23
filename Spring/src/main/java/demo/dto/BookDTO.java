package demo.dto;

import demo.dao.BookRecordsDAO;
import demo.dao.BooksDAOImpl;
import demo.entities.Books;
import org.springframework.beans.factory.annotation.Autowired;
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
        if (booksDAO.isNameFree(name)) {
            return booksDAO.addBookAdmin(isbn, count, name, author, description);
        } else {
            return "Such book already exists";
        }
    }
    public String deleteBook(String name){
        if(booksDAO.isNameFree(name)){
            return "No such book found";
        }else if(bookRecordsDAO.userHistoryExists(booksDAO.getBookByName(name).getIsbn())){
            return "Not all users have returned this book yet. Please, acquire all copies before removing it from the library";
        }else{
            booksDAO.deleteBookAdmin(name);
            return "Success";
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

