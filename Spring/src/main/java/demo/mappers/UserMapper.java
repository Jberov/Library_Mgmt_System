package demo.mappers;

import demo.dto.BookDTO;
import demo.dto.UserDTO;
import demo.entities.Book;
import demo.entities.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@Component
public class UserMapper {
	private final BookMapper bookMapper;
	
	@Autowired
	public UserMapper(BookMapper bookMapper) {
		this.bookMapper = bookMapper;
	}
	
	public UserDTO userToDTO(User user) {
		if (user == null) {
			return null;
		}
		
		UserDTO userDTO = new UserDTO();
		userDTO.setName(user.getName());
		
		return userDTO;
	}
	
	public Map<String, List<BookDTO>> convertMapToDTO(Map<String, List<Book>> map) {
		
		Map<String, List<BookDTO>> mapDTO = new HashMap<>();
		
		List<BookDTO> takenBooks = new LinkedList<>();
		
		List<BookDTO> returnedBooks = new LinkedList<>();
		
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
