package demo.entities;

import demo.status.Status;

import javax.persistence.*;

@Entity
@Table(name = "activity")
public class BooksActivity {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	
	@ManyToOne
	private User user;
	
	@ManyToOne
	private Book book;
	
	@Enumerated(EnumType.ORDINAL)
	private Status status;
	
	public BooksActivity() {
	}
	
	public BooksActivity(User user, Book book, Status status) {
		this.user = user;
		this.book = book;
		this.status = status;
	}
	
	public User getUser() {
		return user;
	}
	
	public void setUser(User user) {
		this.user = user;
	}
	
	public Book getBook() {
		return book;
	}
	
	public void setBook(Book book) {
		this.book = book;
	}
	
	public Status getStatus() {
		return status;
	}
	
	public void setStatus(Status status) {
		this.status = status;
	}
}