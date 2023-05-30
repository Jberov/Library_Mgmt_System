package demo.mappers;

import demo.dto.UserDTO;
import demo.entities.User;
import demo.enums.UserRole;
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
		userDTO.setRole(user.getRole().toString());
		
		return userDTO;
	}

	public User userDTOToEntity(UserDTO userDTO){
		if (userDTO == null) {
			return null;
		}

		if (userDTO.getRole().equals("USER")) {
			return new User(userDTO.getUsername(),UserRole.USER, userDTO.getEmail(), userDTO.getPassword(), userDTO.getTelephoneNumber(),
					userDTO.getAddress(), userDTO.getFirstName(), userDTO.getMidName(), userDTO.getLastName());
		}
		return new User(userDTO.getUsername(),UserRole.ADMIN, userDTO.getEmail(), userDTO.getPassword(), userDTO.getTelephoneNumber(),
				userDTO.getAddress(), userDTO.getFirstName(), userDTO.getMidName(), userDTO.getLastName());
	}
}
