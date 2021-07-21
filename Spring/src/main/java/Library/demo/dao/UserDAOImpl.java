package Library.demo.dao;

import Library.demo.entities.Books;
import Library.demo.entities.Users;
import Library.demo.repositories.UserRepository;
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
    private LinkedList<LinkedList<Books>> usageHistory(String name){
        return  bookRecordsDAO.booksUsedByUser(name);
    }


}
