package demo.entities;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "users")
public class User {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	
	@Column(unique = true)
	private String name;
	
	@ElementCollection
	@Column
	@GeneratedValue
	private List<String> userHistoryOfDeletedBooks;
	
	public User(String name) {
		this.name = name;
	}
	
	public User() {
	}
	
	public List<String> getUserHistoryOfDeletedBooks() {
		return userHistoryOfDeletedBooks;
	}
	
	public void setUserHistoryOfDeletedBooks(List<String> userHistoryOfDeletedBooks) {
		this.userHistoryOfDeletedBooks = userHistoryOfDeletedBooks;
	}
	
	public long getId() {
		return id;
	}
	
	public void setId(long id) {
		this.id = id;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
}