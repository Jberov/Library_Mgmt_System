package demo.dto;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import java.util.List;
import java.util.Map;

public class UserDTO {
	
	@Null
	private long id;
	
	@NotNull
	private String name;
	
	public UserDTO(String name) {
		this.name = name;
	}
	
	public UserDTO() {
	}
	
	public void setUserHistory(Map<String, List<BookDTO>> userHistory) {
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
}
