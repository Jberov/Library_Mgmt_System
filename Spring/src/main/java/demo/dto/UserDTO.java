package demo.dto;

import java.util.List;
import java.util.Map;
import javax.validation.constraints.NotBlank;

public class UserDTO {
	
	@NotBlank(message = "No email")
	private String email;

	public void setUsername(String username) {
		this.username = username;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public void setTelephoneNumber(String telephoneNumber) {
		this.telephoneNumber = telephoneNumber;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public void setMidName(String midName) {
		this.midName = midName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	@NotBlank(message = "No username")
	private String username;

	@NotBlank(message = "No password")
	private String password;

	@NotBlank(message = "No number")
	private String telephoneNumber;

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	private boolean enabled;

	public void setRole(String role) {
		this.role = role;
	}

	@NotBlank(message = "No user role")
	private String role;


	public String getUsername() {
		return username;
	}

	public String getPassword() {
		return password;
	}

	public String getTelephoneNumber() {
		return telephoneNumber;
	}

	public String getAddress() {
		return address;
	}

	public String getFirstName() {
		return firstName;
	}

	public String getMidName() {
		return midName;
	}

	public String getLastName() {
		return lastName;
	}

	@NotBlank(message = "No address")
	private String address;

	@NotBlank(message = "No first name")
	private String firstName;

	@NotBlank(message = "No mid name")
	private String midName;

	public UserDTO(String email, String username, String password, String telephoneNumber, String role, String address,
			String firstName, String midName, String lastName, Map<String, List<String>> userHistory) {
		this.email = email;
		this.username = username;
		this.password = password;
		this.telephoneNumber = telephoneNumber;
		this.role = role;
		this.address = address;
		this.firstName = firstName;
		this.midName = midName;
		this.lastName = lastName;
		this.userHistory = userHistory;
	}

	@NotBlank(message = "No last name")
	private String lastName;


	private Map<String, List<String>> userHistory;
	
	public UserDTO(String email) {
		this.email = email;
	}
	
	public UserDTO() {
	}
	
	public Map<String, List<String>> getUserHistory() {
		return userHistory;
	}
	
	public void setUserHistory(Map<String, List<String>> userHistory) {
		this.userHistory = userHistory;
	}
	
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getRole() {
		return role;
	}
}
