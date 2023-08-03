package demo.dto;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.PositiveOrZero;

public class BookDTO {
	
	@Pattern(regexp = "([97(8|9)]{3}[-][0-9]{1,5}[-][0-9]{0,7}[-][0-9]{0,6}[-][0-9])|([0-9]{13})")
	@NotNull
	private String isbn;
	
	@PositiveOrZero
	private int count_books;
	
	@NotNull
	private String author;
	
	@NotNull
	private String name;

	private String authorDescription;

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@NotNull
	private String description;

	@NotNull
	private String genre;

	public String getGenreDescription() {
		return genreDescription;
	}

	public void setGenreDescription(String genreDescription) {
		this.genreDescription = genreDescription;
	}

	public BookDTO(String isbn, int count_books, String author, String name, String authorDescription, String genre,
			String genreDescription, String description) {
		this.isbn = isbn;
		this.count_books = count_books;
		this.author = author;
		this.name = name;
		this.authorDescription = authorDescription;
		this.genre = genre;
		this.genreDescription = genreDescription;
		this.description = description;
	}

	private String genreDescription;
	
	public BookDTO(String isbn, int count_books, String author, String name, String authorDescription, String genre) {
		this.isbn = isbn;
		this.count_books = count_books;
		this.author = author;
		this.name = name;
		this.authorDescription = authorDescription;
		this.genre = genre;
	}
	
	public BookDTO() {
	}
	
	public String getIsbn() {
		return isbn;
	}
	
	public void setIsbn(String isbn) {
		this.isbn = isbn;
	}
	
	public int getCount() {
		return count_books;
	}
	
	public void setCount(int count) {
		this.count_books = count;
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
	
	public String getAuthorDescription() {
		return authorDescription;
	}
	
	public void setAuthorDescription(String authorDescription) {
		this.authorDescription = authorDescription;
	}

	public String getGenre() {
		return genre;
	}

	public void setGenre(String genre) {
		this.genre = genre;
	}
}
