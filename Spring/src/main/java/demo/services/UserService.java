package demo.services;

import demo.dao.BookRecordsDAO;
import demo.dao.BooksDAOImpl;
import demo.dao.UserDAOImpl;
import demo.dto.BookDTO;
import demo.dto.UserDTO;
import demo.entities.User;
import demo.entities.VerificationToken;
import demo.mappers.BookMapper;
import demo.mappers.UserMapper;
import demo.repositories.VerificationTokenRepository;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService {
	private final UserDAOImpl userDAO;
	private final BooksDAOImpl booksDAO;
	private final BookRecordsDAO bookRecordsDAO;
	private final UserMapper userMapper;
	private final BookMapper bookMapper;
	private final PasswordEncoder passwordEncoder;
	private final VerificationTokenRepository tokenRepository;


	@Autowired
	public UserService(PasswordEncoder passwordEncoder, UserDAOImpl userDAO, BooksDAOImpl booksDAO, BookRecordsDAO bookRecordsDAO, UserMapper userMapper, BookMapper bookMapper, VerificationTokenRepository tokenRepository) {
		this.userDAO = userDAO;
		this.booksDAO = booksDAO;
		this.bookRecordsDAO = bookRecordsDAO;
		this.userMapper = userMapper;
		this.bookMapper = bookMapper;
		this.passwordEncoder = passwordEncoder;
		this.tokenRepository = tokenRepository;
	}

	public List<UserDTO> getAllUsers() throws NullPointerException{
		List<UserDTO> users = new ArrayList<>();

		if (userDAO.getAllUsers().isEmpty() || userDAO.getAllUsers() == null){
			throw new NullPointerException("No users");
		}

		for(User user : userDAO.getAllUsers()){
			users.add(userMapper.userToDTO(user));
		}
		return users;
	}
	
	public UserDTO getUser(String name) {
		if (userDAO.findUserByName(name) == null) {
			return null;
		}
		UserDTO userDTO = userMapper.userToDTO(userDAO.findUserByName(name));
		userDTO.setUserHistory(userUsedBooks(name));
		return userDTO;
	}

	public boolean userExistsByMail(String mail) {
		return userDAO.isUserByMail(mail);
	}

	public boolean userExistsByPhone(String phone) {
		return userDAO.isUserByPhone(phone);
	}
	
	public BookDTO leaseBook(String isbn, String username) {
		if (booksDAO.getBook(isbn) == null) {
			return null;
		} else if (!booksDAO.bookExistsByID(isbn) || booksDAO.getCount(isbn) <= 0) {
			return null;
		} else if (userDAO.findUserByName(username) == null) {
			throw new NoSuchElementException("No such User");
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
		if (!userDAO.isUser(username)){
			throw new NullPointerException();
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

	public void deleteToken(String token) {
		userDAO.deleteToken(token);
	}

	public UserDTO createUser(UserDTO userDTO) throws IllegalArgumentException {
		if (getUser(userDTO.getUsername()) != null || getUser(userDTO.getUsername()) != null){
			throw new IllegalArgumentException();
		}
		encodePassword(userDTO);
		userDAO.addUser(userMapper.userDTOToEntity(userDTO));
		return userDTO;
	}

	public void enableUser(String username){
		User user = userDAO.findUserByName(username);
		user.setEnabled(true);
		userDAO.updateUser(username, user);
	}

	public UserDTO updateUser(String username, UserDTO userDTO) throws IllegalArgumentException {
		UserDTO localUser = getUser(username);
		if (localUser == null){
			throw new NullPointerException();
		}

		//In case there is password change
		if (!userDTO.getPassword().equals(localUser.getPassword())){
			encodePassword(userDTO);
		}

		userDAO.updateUser(username, userMapper.userDTOToEntity(userDTO));
		return userDTO;
	}

	private void encodePassword(UserDTO userDTO){
		String encodedPassword = this.passwordEncoder.encode(userDTO.getPassword());
		userDTO.setPassword(encodedPassword);
	}

	@Transactional
	public void createVerificationToken(User userReceived, String token) {
		User user = userDAO.findUserByName(userReceived.getUsername());
		VerificationToken myToken = new VerificationToken(token, user);
		tokenRepository.save(myToken);
	}

	public VerificationToken getVerificationToken(String VerificationToken) {
		return tokenRepository.findByToken(VerificationToken);
	}

	@Transactional
	public void invalidateToken(String VerificationToken){
		VerificationToken token = tokenRepository.findByToken(VerificationToken);
		token.setExpiryDate(new Date());
		tokenRepository.save(token);
	}
}


