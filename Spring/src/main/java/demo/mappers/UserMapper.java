package demo.mappers;

import demo.dto.BookDTO;
import demo.dto.UserDTO;
import demo.entities.Books;
import demo.entities.Users;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.LinkedList;

@Component
public class UserMapper {

    @Autowired
    private  BookMapper bookMapper;

    public UserDTO userToDTO(Users user){
        if (user == null) {
            return null;
        }
        UserDTO userDTO = new UserDTO();
        userDTO.setName(user.getName());
        return userDTO;
    }

    public HashMap<String, LinkedList<BookDTO>> convertMapToDTO(HashMap<String, LinkedList<Books>> map){
        HashMap<String, LinkedList<BookDTO>> mapDTO  = new HashMap<>();
        LinkedList<BookDTO> list = new LinkedList<>();
        for(Books i : map.get("Currently taken books by user:")){
            list.add(bookMapper.bookToDTO(i));
        }
        mapDTO.put("Currently taken books by user:",list);
        list.clear();
        for(Books i : map.get("Already returned books by user:")){
            list.add(bookMapper.bookToDTO(i));
        }
        mapDTO.put("Already returned books by user:",list);
        return mapDTO;
    }
}
