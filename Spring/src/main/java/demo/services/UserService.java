package demo.services;

import demo.dao.BookRecordsDAO;
import demo.dao.BooksDAOImpl;
import demo.dao.UserDAOImpl;
import demo.dto.BookDTO;
import demo.dto.UserDTO;
import demo.mappers.BookMapper;
import demo.mappers.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    private BookMapper bookMapper;

    public UserDTO getUser(String name) {
        return userMapper.userToDTO(userDAO.findUserByName(name));
    }

    public BookDTO leaseBook (String isbn, String username) {
        if (!booksDAO.bookExistsByID(isbn) || (!booksDAO.getBook(isbn).isExists()) || booksDAO.checkCount(isbn) <= 0 ) {
            return null;
        } else if(userDAO.UserExists(username) == null) {
            userDAO.addUsers(username);
            return bookMapper.bookToDTO(bookRecordsDAO.leaseBook(isbn, username));
        } else if(bookRecordsDAO.checkIfUserHasTakenBook(isbn, username)) {
            return bookMapper.bookToDTO(bookRecordsDAO.leaseBook(isbn, username));
        }
        return bookMapper.bookToDTO(bookRecordsDAO.leaseBook(isbn, username));

    }

    public BookDTO returnBook (String isbn, String username) {
        if (userDAO.findUserByName(username) == null) {
            return null;
        } else if (!bookRecordsDAO.checkIfUserHasTakenBook(isbn, username)) {
            return null;
        } else if (!booksDAO.bookExistsByID(isbn) || !booksDAO.getBook(isbn).isExists()) {
            return null;
        }
        return bookMapper.bookToDTO(bookRecordsDAO.returnBook(isbn, username));
    }

    public HashMap<String,LinkedList<BookDTO>> userUsedBooks (String username) {
        if ((userDAO.findUserByName(username) == null) || (bookRecordsDAO.booksUsedByUser(username) == null)) {
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


