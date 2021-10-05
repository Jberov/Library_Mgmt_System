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

import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;

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
		} else if (!booksDAO.bookExistsByID(isbn) || booksDAO.getCount(isbn) <= 0) {
			return null;
		} else if (userDAO.findUserByName(username) == null) {
			userDAO.addUser(username);
			return bookMapper.bookToDTO(bookRecordsDAO.leaseBook(isbn, username));
		} else if (bookRecordsDAO.checkIfUserHasTakenBook(isbn, username)) {
			return bookMapper.bookToDTO(bookRecordsDAO.leaseBook(isbn, username));
		}
		return bookMapper.bookToDTO(bookRecordsDAO.leaseBook(isbn, username));
		
	}
	
	public BookDTO returnBook(String isbn, String username) {
		if ((userDAO.findUserByName(username) == null) || (!bookRecordsDAO.checkIfUserHasTakenBook(isbn, username)) || (!booksDAO.bookExistsByID(isbn))) {
			return null;
		}
		return bookMapper.bookToDTO(bookRecordsDAO.returnBook(isbn, username));
	}
	
	public Map<String, List<String>> userUsedBooks(String username) {
		Map<String, List<String>> history = bookRecordsDAO.booksUsedByUser(username);
		if ((!userDAO.isUser(username)) || (history == null)) {
			return null;
		}
		return history;
	}
	
	public Set<String> getUsersByBook(String isbn) {
		if (booksDAO.bookExistsByID(isbn)) {
			return bookRecordsDAO.getUsersByBook(isbn);
		}
		return null;
	}
	public UserDTO deleteUser(String username) throws NoSuchElementException {
		return userMapper.userToDTO(userDAO.deleteUser(username));
	}
}


