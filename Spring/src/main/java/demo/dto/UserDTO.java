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
	
	private Map<String, List<String>> userHistory;
	
	public UserDTO(String name) {
		this.name = name;
	}
	
	public UserDTO() {
	}
	
	public long getId() {
		return id;
	}
	
	public void setId(long id) {
		this.id = id;
	}
	
	public Map<String, List<String>> getUserHistory() {
		return userHistory;
	}
	
	public void setUserHistory(Map<String, List<String>> userHistory) {
		this.userHistory = userHistory;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
}
