package demo.dao;

import demo.entities.User;
import demo.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserDAOImpl {
	private final UserRepository userRepository;
	
	@Autowired
	public UserDAOImpl(UserRepository userRepository) {
		this.userRepository = userRepository;
	}
	
	public User findUserByName(String name) {
		return userRepository.findByName(name);
	}
	
	public void addUsers(String username) {
		userRepository.save(new User(username));
	}
	
	public User UserExists(String username) {
		return userRepository.findByName(username);
	}
}
