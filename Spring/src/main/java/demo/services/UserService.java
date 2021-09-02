package demo.services;

import demo.dao.BookRecordsDAO;
import demo.dao.BooksDAOImpl;
import demo.dao.UserDAOImpl;
import demo.dto.BookDTO;
import demo.dto.UserDTO;
import demo.entities.Books;
import demo.entities.Users;
import demo.mappers.BookMapper;
import demo.mappers.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.LinkedList;

@Service
public class UserService {
    @Autowired
    private UserDAOImpl userDAO;
    @Autowired
    private BooksDAOImpl booksDAO;
    @Autowired
    private BookRecordsDAO bookRecordsDAO;

    @Autowired
    private UserMapper userMapper;

    public UserDTO getUser(String name) {
        return userMapper.userToDTO(userDAO.findUserByName(name));
    }

    public String leaseBook (String isbn, String username) {
        if (!booksDAO.bookExistsByID(isbn) || (!booksDAO.getBook(isbn).isExists() )) {
            return "No such book";
        } else if (booksDAO.checkCount(isbn) <= 0 ) {
            return "No copies of this book";
        } else if(userDAO.UserExists(username) == null) {
            userDAO.addUsers(username);
            booksDAO.decreaseCount(isbn);
            bookRecordsDAO.leaseBook(isbn, username);
            return "User created. Lease successful";
        } else if(bookRecordsDAO.checkIfUserHasTakenBook(isbn, username)) {
            booksDAO.decreaseCount(isbn);
            bookRecordsDAO.leaseBook(isbn, username);
            return "Another copy successfully fetched";
        }
        return bookRecordsDAO.leaseBook(isbn, username);

    }

    public String returnBook (String isbn, String username) {
        if (userDAO.findUserByName(username) == null) {
            return "No such user";
        } else if (!bookRecordsDAO.checkIfUserHasTakenBook(isbn, username)) {
            return "You have not taken this book!";
        } else if (!booksDAO.bookExistsByID(isbn) || !booksDAO.getBook(isbn).isExists()) {
            return "No such book exists";
        }
        return bookRecordsDAO.returnBook(isbn, username);
    }

    public HashMap<String,LinkedList<BookDTO>> userUsedBooks (String username) {
        if ((userDAO.findUserByName(username) == null) || (bookRecordsDAO.booksUsedByUser(username).isEmpty())) {
            return null;
        }
        return userMapper.convertMapToDTO(bookRecordsDAO.booksUsedByUser(username));
    }
    public LinkedList<String> getUsersByBook (String isbn) {
        if (booksDAO.bookExistsByID(isbn)) {
            return bookRecordsDAO.getUsersByBook(isbn);
        }
        return null;
    }
}


