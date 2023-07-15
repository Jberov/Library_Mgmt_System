package demo.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.Pattern;

@Entity
@Table(name = "book")
public class Book {
	
	@Id
	@Column(nullable = false)
	@Pattern(regexp = "([97(8|9)]{3}[-][0-9]{1,5}[-][0-9]{0,7}[-][0-9]{0,6}[-][0-9])|([0-9]{13})")
	private String isbn;

	@Column(nullable = false)
	private int countBooks;

	@OneToOne
	private Author author;

	public Genre getGenre() {
		return genre;
	}

	@ManyToOne
	private Genre genre;
	
	@Column(unique = true, nullable = false)
	private String name;

	@Column(nullable = false)
	private String description;
	
	public Book(String isbn, int countBooks, Author author, String name, String description, Genre genre) {
		this.isbn = isbn;
		this.countBooks = countBooks;
		this.author = author;
		this.name = name;
		this.description = description;
		this.genre = genre;
	}
	
	public Book() {
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
	
	public Author getAuthor() {
		return author;
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
}
