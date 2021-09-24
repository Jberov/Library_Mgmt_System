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
		return userRepository.findByName(name);
	}
	
	public void saveBookToHistory(User user, String book) {
		List<String> bookSanctuary = user.getUserHistoryOfDeletedBooks();
		bookSanctuary.add(book);
		user.setUserHistoryOfDeletedBooks(bookSanctuary);
		userRepository.save(user);
	}
	
	public boolean isUser(String username) {
		return userRepository.existsByName(username);
	}
	
	public void addUser(String username) {
		userRepository.save(new User(username));
	}
	
	public User deleteUser(String username){
		User user = userRepository.findByName(username);
		if(user != null){
			cleanUserRecords(username);
			userRepository.delete(userRepository.findByName(username));
			return user;
		}
		throw new NoSuchElementException();
	}
	
	private void cleanUserRecords(String username){
		List<BooksActivity> records = bookRecordsRepository.findByUserId(userRepository.findByName(username).getId());
		for(BooksActivity record : records){
			bookRecordsRepository.delete(record);
		}
	}
}
