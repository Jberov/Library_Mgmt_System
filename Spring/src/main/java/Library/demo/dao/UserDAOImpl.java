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
    UserRepository userRepository;

    /*public Users getUser(long id){
        return userRepository.findById(id).get();
    }*/
    public Users findUserByName(String name){
        return userRepository.findByName(name);
    }


}
