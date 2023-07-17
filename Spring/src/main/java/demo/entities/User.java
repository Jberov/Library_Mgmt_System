package demo.entities;

import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "users")
public class User {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	
	@Column(unique = true)
	private String username;

	@ManyToOne(fetch = FetchType.EAGER, cascade= CascadeType.ALL)
	@JoinColumn(name = "roleId")
	private UserRoles role;

	public User(String username, UserRoles role, String email, String password, String telephoneNumber, String address,
			String firstName, String midName, String lastName) {
		this.username = username;
		this.role = role;
		this.email = email;
		this.password = password;
		this.telephoneNumber = telephoneNumber;
		this.address = address;
		this.firstName = firstName;
		this.midName = midName;
		this.lastName = lastName;
		this.enabled = false;
	}

	@Column(unique = true)
	private String email;

	@Column(unique = true)
	private String password;

	@Column(unique = true)
	private String telephoneNumber;

	@Column
	private String address;

	@Column
	private String firstName;

	@Column
	private String midName;

	@Column
	private String lastName;

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	@Column
	private boolean enabled;
	
	@ElementCollection(fetch = FetchType.EAGER)
	@Column
	private List<String> userHistoryOfDeletedBooks;
	
	public User(String username) {
		this.username = username;
	}
	
	public User() {
	}
	
	public List<String> getUserHistoryOfDeletedBooks() {
		return userHistoryOfDeletedBooks;
	}
	
	public void setUserHistoryOfDeletedBooks(List<String> userHistoryOfDeletedBooks) {
		this.userHistoryOfDeletedBooks = userHistoryOfDeletedBooks;
	}

	public String getEmail() {
		return email;
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

	public long getId() {
		return id;
	}

	public String getUsername() {
		return username;
	}
	
	public void setUsername(String username) {
		this.username = username;
	}

	public void setEmail(String email) {
		this.email = email;
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

	public UserRoles getRole() {
		return role;
	}

	public void setRole(UserRoles role) {
		this.role = role;
	}
}
