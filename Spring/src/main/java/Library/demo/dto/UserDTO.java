package Library.demo.dto;

import Library.demo.dao.UserDAOImpl;
import Library.demo.entities.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserDTO {
    @Autowired
    UserDAOImpl userDAO;

    public User getUser(String name){
        if(userDAO.findUserByName(name) != null){
            return userDAO.findUserByName(name);
        }else{
            return null;
        }
    }
}
