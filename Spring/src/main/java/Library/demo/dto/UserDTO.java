package Library.demo.dto;

import Library.demo.dao.UserDAOImpl;
import Library.demo.entities.Users;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserDTO {
    @Autowired
    private UserDAOImpl userDAO;

    public Users getUser(String name){
        if(userDAO.findUserByName(name) != null){
            return userDAO.findUserByName(name);
        }else{
            return null;
        }
    }
}
