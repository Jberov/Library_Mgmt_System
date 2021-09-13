package demo.entities;

import javax.persistence.*;
import java.util.HashMap;
import java.util.LinkedList;

@Entity
@Table(name = "users")
public class User {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	
	@Column(unique = true)
	private String name;
	@Transient
	private HashMap<String, LinkedList<Book>> userHistory;
	
	public User(String name) {
		this.name = name;
	}
	
	public User() {
	}
	
	public HashMap<String, LinkedList<Book>> getUserHistory() {
		return userHistory;
	}
	
	public void setUserHistory(HashMap<String, LinkedList<Book>> userHistory) {
		this.userHistory = userHistory;
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