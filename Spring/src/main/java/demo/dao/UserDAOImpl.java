package demo.dao;

import demo.entities.BooksActivity;
import demo.entities.User;
import demo.repositories.BookRecordsRepository;
import demo.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class UserDAOImpl {
	private final UserRepository userRepository;
	
	private final BookRecordsRepository bookRecordsRepository;
	
	@Autowired
	public UserDAOImpl(UserRepository userRepository, BookRecordsRepository bookRecordsRepository) {
		this.userRepository = userRepository;
		this.bookRecordsRepository = bookRecordsRepository;
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
	
	public void addUser(User user) {
		userRepository.save(user);
	}

	public void updateUser(String username, User user) {
		User userToUpdate = userRepository.findByUsername(username);
		userToUpdate.setEmail(user.getEmail());
		userToUpdate.setUsername(user.getUsername());
		userToUpdate.setPassword(user.getPassword());
		userToUpdate.setTelephoneNumber(user.getTelephoneNumber());
		userToUpdate.setAddress(user.getAddress());
		userToUpdate.setFirstName(user.getFirstName());
		userToUpdate.setMidName(user.getMidName());
		userToUpdate.setLastName(user.getLastName());
		userRepository.save(userToUpdate);
	}
	
	public User deleteUser(String username){
		User user = userRepository.findByUsername(username);
		if(user != null){
			cleanUserRecords(username);
			userRepository.delete(userRepository.findByUsername(username));
			return user;
		}
		throw new NoSuchElementException();
	}
	
	private void cleanUserRecords(String username){
		List<BooksActivity> records = bookRecordsRepository.findByUserId(userRepository.findByUsername(username).getId());
		for(BooksActivity record : records){
			bookRecordsRepository.delete(record);
		}
	}

	public List<User> getAllUsers(){
		return userRepository.findAll();
	}
}
