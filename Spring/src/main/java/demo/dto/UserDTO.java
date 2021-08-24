package demo.dto;

import demo.dao.BookRecordsDAO;
import demo.dao.BooksDAOImpl;
import demo.dao.UserDAOImpl;
import demo.entities.Books;
import demo.entities.Users;
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

    public String leaseBook(String isbn, String username){
        if(!booksDAO.bookExistsByID(isbn) || (!booksDAO.getBook(isbn).isExists() )){
            return "No such book";
        }else if(booksDAO.checkCount(isbn) <=0 ){
            return "No copies of this book";
        }else if(userDAO.UserExists(username) == null){
            userDAO.addUsers(username);
            return bookRecordsDAO.leaseBook(isbn, username);
        }else if(bookRecordsDAO.checkIfUserHasTakenBook(isbn, username)){
            booksDAO.decreaseCount(isbn);
            bookRecordsDAO.leaseBook(isbn, username);
            return "Another copy successfully fetched";
        }else{
            return bookRecordsDAO.leaseBook(isbn, username);
        }
    }

    public String returnBook(String isbn, String username){
        if(userDAO.findUserByName(username) == null){
            return "No such user";
        }else if(!bookRecordsDAO.checkIfUserHasTakenBook(isbn, username)){
            return "You have not taken this book!";
        }else if(!booksDAO.bookExistsByID(isbn) || !booksDAO.getBook(isbn).isExists()){
            return "No such book exists";
        }else{
            return bookRecordsDAO.returnBook(isbn, username);
        }
    }

    public LinkedList<LinkedList<Books>> userUsedBooks(String username){
        if(userDAO.findUserByName(username) == null){
            return null;
        }else if(bookRecordsDAO.booksUsedByUser(username).isEmpty()){
            return null;
        }else if(userDAO.findUserByName(username) == null){
            return null;
        }else{
            return bookRecordsDAO.booksUsedByUser(username);
        }

    }

    public LinkedList<String> getUsersByBook(String isbn){
        if(booksDAO.bookExistsByID(isbn)){
            return bookRecordsDAO.getUsersByBook(isbn);
        }else{
            return null;
        }
    }
}


