package demo.dao;

import demo.entities.BooksActivity;
import demo.entities.User;
import demo.entities.VerificationToken;
import demo.enums.Status;
import demo.repositories.BookRecordsRepository;
import demo.repositories.UserRepository;
import demo.repositories.VerificationTokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class UserDAOImpl {
	private final UserRepository userRepository;
	
	private final BookRecordsRepository bookRecordsRepository;

	private final VerificationTokenRepository tokenRepository;
	
	@Autowired
	public UserDAOImpl(UserRepository userRepository, BookRecordsRepository bookRecordsRepository, VerificationTokenRepository tokenRepository) {
		this.userRepository = userRepository;
		this.bookRecordsRepository = bookRecordsRepository;
		this.tokenRepository = tokenRepository;
	}
	
	public User findUserByName(String name) {
		return userRepository.findByUsername(name);
	}
	
	public void saveBookToHistory(User user, String book) {
		List<String> bookSanctuary = user.getUserHistoryOfDeletedBooks();
		bookSanctuary.add(book);
		user.setUserHistoryOfDeletedBooks(bookSanctuary);
		userRepository.save(user);
	}
	
	public boolean isUser(String username) {
		return userRepository.existsByUsername(username);
	}

	public boolean isUserByMail(String email) {
		return userRepository.existsByEmail(email);
	}

	public boolean isUserByPhone(String phone) {
		return userRepository.existsByTelephoneNumber(phone);
	}
	
	public void addUser(User user) {
		userRepository.save(user);
	}

	public void updateUser(String username, User user) {
		User userToUpdate = userRepository.findByUsername(username);
		userToUpdate.setEmail(user.getEmail());
		userToUpdate.setRole(user.getRole());
		userToUpdate.setUsername(user.getUsername());
		userToUpdate.setPassword(user.getPassword());
		userToUpdate.setTelephoneNumber(user.getTelephoneNumber());
		userToUpdate.setAddress(user.getAddress());
		userToUpdate.setFirstName(user.getFirstName());
		userToUpdate.setMidName(user.getMidName());
		userToUpdate.setLastName(user.getLastName());
		userToUpdate.setEnabled(user.isEnabled());
		userRepository.save(userToUpdate);
	}

	public User deleteUser(String username){
		User user = userRepository.findByUsername(username);
		if (user != null){
			cleanUserRecords(username);
			userRepository.delete(user);
			return user;
		}
		throw new NoSuchElementException();
	}

	public void deleteToken(String token) {
		VerificationToken token1 = tokenRepository.findByToken(token);
		tokenRepository.delete(token1);
	}
	
	private void cleanUserRecords(String username){
		List<BooksActivity> records = bookRecordsRepository.findByUserId(userRepository.findByUsername(username).getId());
		for(BooksActivity record : records){
			if(record.getStatus().equals(Status.TAKEN)) {
				throw new IllegalArgumentException("Потребителят не е върнал всички книги");
			}
			bookRecordsRepository.delete(record);
		}
	}

	public List<User> getAllUsers(){
		return userRepository.findAll();
	}
}
