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
import java.util.List;
import java.util.Map;

@Service
public class UserService {
	private final UserDAOImpl userDAO;
	private final BooksDAOImpl booksDAO;
	private final BookRecordsDAO bookRecordsDAO;
	private final UserMapper userMapper;
	private final BookMapper bookMapper;
	
	@Autowired
	public UserService(UserDAOImpl userDAO, BooksDAOImpl booksDAO, BookRecordsDAO bookRecordsDAO, UserMapper userMapper, BookMapper bookMapper) {
		this.userDAO = userDAO;
		this.booksDAO = booksDAO;
		this.bookRecordsDAO = bookRecordsDAO;
		this.userMapper = userMapper;
		this.bookMapper = bookMapper;
	}
	
	public UserDTO getUser(String name) {
		if (userDAO.findUserByName(name) == null) {
			return null;
		}
		UserDTO userDTO = userMapper.userToDTO(userDAO.findUserByName(name));
		userDTO.setUserHistory(userUsedBooks(name));
		return userDTO;
	}
	
	public BookDTO leaseBook(String isbn, String username) {
		
		if (booksDAO.getBook(isbn) == null) {
			return null;
		} else if (!booksDAO.bookExistsByID(isbn) || (!booksDAO.getBook(isbn).isExists()) || booksDAO.checkCount(isbn) <= 0) {
			return null;
		} else if (userDAO.UserExists(username) == null) {
			userDAO.addUsers(username);
			return bookMapper.bookToDTO(bookRecordsDAO.leaseBook(isbn, username));
		} else if (bookRecordsDAO.checkIfUserHasTakenBook(isbn, username)) {
			return bookMapper.bookToDTO(bookRecordsDAO.leaseBook(isbn, username));
		}
		return bookMapper.bookToDTO(bookRecordsDAO.leaseBook(isbn, username));
		
	}
	
	public BookDTO returnBook(String isbn, String username) {
		if ((userDAO.findUserByName(username) == null) || (!bookRecordsDAO.checkIfUserHasTakenBook(isbn, username)) || (!booksDAO.bookExistsByID(isbn) || !booksDAO.getBook(isbn).isExists())) {
			return null;
		}
		return bookMapper.bookToDTO(bookRecordsDAO.returnBook(isbn, username));
	}
	
	public Map<String, List<BookDTO>> userUsedBooks(String username) {
		if ((userDAO.findUserByName(username) == null) || (bookRecordsDAO.booksUsedByUser(username) == null)) {
			return null;
		}
		return userMapper.convertMapToDTO(bookRecordsDAO.booksUsedByUser(username));
	}
	
	public LinkedList<String> getUsersByBook(String isbn) {
		if (booksDAO.bookExistsByID(isbn)) {
			return bookRecordsDAO.getUsersByBook(isbn);
		}
		return null;
	}
}


