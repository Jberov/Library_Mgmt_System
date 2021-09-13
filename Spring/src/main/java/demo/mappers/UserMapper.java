package demo.mappers;

import demo.dto.BookDTO;
import demo.dto.UserDTO;
import demo.entities.Book;
import demo.entities.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.LinkedList;

@Component
public class UserMapper {
	
	@Autowired
	private BookMapper bookMapper;
	
	public UserDTO userToDTO(User user) {
		if (user == null) {
			return null;
		}
		
		UserDTO userDTO = new UserDTO();
		userDTO.setName(user.getName());
		userDTO.setUserHistory(convertMapToDTO(user.getUserHistory()));
		
		return userDTO;
	}
	
	public HashMap<String, LinkedList<BookDTO>> convertMapToDTO(HashMap<String, LinkedList<Book>> map) {
		
		HashMap<String, LinkedList<BookDTO>> mapDTO = new HashMap<>();
		
		LinkedList<BookDTO> takenBooks = new LinkedList<>();
		
		LinkedList<BookDTO> returnedBooks = new LinkedList<>();
		
		for (Book i : map.get("Currently taken books by user:")) {
			takenBooks.add(bookMapper.bookToDTO(i));
		}
		mapDTO.put("Currently taken books by user:", takenBooks);
		
		for (Book i : map.get("Already returned books by user:")) {
			returnedBooks.add(bookMapper.bookToDTO(i));
		}
		mapDTO.put("Already returned books by user:", returnedBooks);
		
		return mapDTO;
	}
}
