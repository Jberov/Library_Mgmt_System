package Library.demo.dao;

import Library.demo.entities.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserDAOImpl {
    @Autowired
    UserRepository userRepository;

    public User getUser(long id){
        return userRepository.findById(id).get();
    }
    public User findUserByName(String name){
        return userRepository.findByName(name);
    }


}
