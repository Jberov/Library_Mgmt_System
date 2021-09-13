package demo.dao;

import demo.entities.Book;
import demo.entities.User;
import demo.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.LinkedList;

@Service
public class UserDAOImpl {
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private BookRecordsDAO bookRecordsDAO;
	
	public User findUserByName(String name) {
		User user = userRepository.findByName(name);
		if (user == null) {
			return null;
		}
		user.setUserHistory(usageHistory(name));
		return user;
	}
	
	public void addUsers(String username) {
		userRepository.save(new User(username));
	}
	
	public User UserExists(String username) {
		return userRepository.findByName(username);
	}
	
	private HashMap<String, LinkedList<Book>> usageHistory(String name) {
		return bookRecordsDAO.booksUsedByUser(name);
	}
}
