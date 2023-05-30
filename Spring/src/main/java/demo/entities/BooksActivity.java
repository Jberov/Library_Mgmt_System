package demo.entities;

import demo.enums.Status;
import java.time.LocalDate;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

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

	public LocalDate getIssueDate() {
		return issueDate;
	}

	public void setIssueDate(LocalDate issueDate) {
		this.issueDate = issueDate;
	}

	public LocalDate getReturnDate() {
		return returnDate;
	}

	public void setReturnDate(LocalDate returnDate) {
		this.returnDate = returnDate;
	}

	public BooksActivity(User user, Book book, Status status, LocalDate issueDate, LocalDate returnDate) {
		this.user = user;
		this.book = book;
		this.status = status;
		this.issueDate = issueDate;
		this.returnDate = returnDate;
	}

	@Column(name = "issueDate", columnDefinition = "DATE", nullable = false)
	private LocalDate issueDate;

	@Column(name = "returnDate", columnDefinition = "DATE",  nullable = false)
	private LocalDate returnDate;


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
