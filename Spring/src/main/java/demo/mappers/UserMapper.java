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
		userDTO.setEmail(user.getEmail());
		userDTO.setUsername(user.getUsername());
		userDTO.setPassword(user.getPassword());
		userDTO.setTelephoneNumber(user.getTelephoneNumber());
		userDTO.setAddress(user.getAddress());
		userDTO.setFirstName(user.getFirstName());
		userDTO.setMidName(user.getMidName());
		userDTO.setLastName(user.getLastName());
		
		return userDTO;
	}

	public User userDTOToEntity(UserDTO userDTO){
		if (userDTO == null) {
			return null;
		}
		return new User(userDTO.getUsername(), userDTO.getEmail(), userDTO.getPassword(), userDTO.getTelephoneNumber(),
				userDTO.getAddress(), userDTO.getFirstName(), userDTO.getMidName(), userDTO.getLastName());
	}
}
