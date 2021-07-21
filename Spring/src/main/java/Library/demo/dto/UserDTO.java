package Library.demo.dto;

import Library.demo.dao.BookRecordsDAO;
import Library.demo.dao.BooksDAOImpl;
import Library.demo.dao.UserDAOImpl;
import Library.demo.entities.Books;
import Library.demo.entities.Users;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.LinkedList;

@Service
public class UserDTO {
    @Autowired
    private UserDAOImpl userDAO;
    @Autowired
    private BooksDAOImpl booksDAO;
    @Autowired
    private BookRecordsDAO bookRecordsDAO;

    public Users getUser(String name){
        if(userDAO.findUserByName(name) != null){
            return userDAO.findUserByName(name);
        }else{
            return null;
        }
    }
    public String leaseBook(long isbn, String username){
        if(booksDAO.checkCount(isbn) <=0 ){
            return "No copies of this book";
        }else if(!booksDAO.bookExistsByID(isbn)){
            return "No such book";
        }else if(userDAO.findUserByName(username) == null){
            return "No such user";
        }else{
            return bookRecordsDAO.leaseBook(isbn, username);
        }
    }
    public String returnBook(long isbn, String username){
        if(!booksDAO.bookExistsByID(isbn)){
            return "No such book has benn taken or does not exist";
        }else if(userDAO.findUserByName(username) == null){
            return "No such user";
        }else{
            return bookRecordsDAO.returnBook(isbn, username);
        }
    }
    public LinkedList<Books> userUsedBooks(String username){
        if(bookRecordsDAO.booksUsedByUser(username).isEmpty()){
            return null;
        }else{
            return bookRecordsDAO.booksUsedByUser(username);
        }
    }
}
