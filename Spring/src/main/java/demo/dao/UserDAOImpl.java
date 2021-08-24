package demo.dao;

import demo.entities.Books;
import demo.entities.Users;
import demo.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.LinkedList;

@Service
public class UserDAOImpl {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private BookRecordsDAO bookRecordsDAO;

    public Users findUserByName(String name){
        Users user =  userRepository.findByName(name);
        user.setUserHistory(usageHistory(name));
        return user;
    }
    public void addUsers(String username){
        userRepository.save(new Users(username));
    }
    public Users UserExists(String username){
        return userRepository.findByName(username);
    }

    private LinkedList<LinkedList<Books>> usageHistory(String name){
        return  bookRecordsDAO.booksUsedByUser(name);
    }
}
