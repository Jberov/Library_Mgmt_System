package demo.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Pattern;

@Entity
@Table(name = "book")
public class Book {
	
	@Id
	@Pattern(regexp = "([97(8|9)]{3}[-][0-9]{1,5}[-][0-9]{0,7}[-][0-9]{0,6}[-][0-9])|([0-9]{13})")
	private String isbn;
	
	@Column
	private int countBooks;
	
	@Column
	private String author;
	
	@Column(unique = true)
	private String name;
	
	@Column
	private String description;
	
	@Column
	private boolean existence;
	
	public Book(String isbn, int countBooks, String author, String name, String description, boolean existence) {
		this.isbn = isbn;
		this.countBooks = countBooks;
		this.author = author;
		this.name = name;
		this.description = description;
		this.existence = existence;
	}
	
	public Book() {
	}
	
	public boolean isExists() {
		return existence;
	}
	
	public void setExists(boolean exists) {
		this.existence = exists;
	}
	
	public String getIsbn() {
		return isbn;
	}
	
	public void setIsbn(String isbn) {
		this.isbn = isbn;
	}
	
	public int getCount() {
		return countBooks;
	}
	
	public void setCount(int count) {
		this.countBooks = count;
	}
	
	public String getAuthor() {
		return author;
	}
	
	public void setAuthor(String author) {
		this.author = author;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getDescription() {
		return description;
	}
	
	public void setDescription(String description) {
		this.description = description;
	}
}
