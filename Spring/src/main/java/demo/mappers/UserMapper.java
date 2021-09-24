package demo.mappers;

import demo.dto.UserDTO;
import demo.entities.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {
	
	public UserDTO userToDTO(User user) {
		if (user == null) {
			return null;
		}
		
		UserDTO userDTO = new UserDTO();
		userDTO.setName(user.getName());
		
		return userDTO;
	}
}
