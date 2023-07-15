package demo.mappers;

import demo.dto.BookDTO;
import demo.entities.Author;
import demo.entities.Book;
import demo.entities.Genre;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class BookMapper {
	public BookDTO bookToDTO(Book book) {
		if (book == null) {
			return null;
		}
		
		BookDTO bookDTO = new BookDTO();
		bookDTO.setIsbn(book.getIsbn());
		bookDTO.setCount(book.getCount());
		bookDTO.setAuthor(book.getAuthor().getName());
		bookDTO.setAuthorDescription(book.getAuthor().getDescription());
		bookDTO.setName(book.getName());
		bookDTO.setAuthorDescription(book.getDescription());
		bookDTO.setGenre(book.getGenre().getGenre());
		bookDTO.setGenreDescription(book.getGenre().getDescription());
		return bookDTO;
	}
	
	public Book bookToEntity(BookDTO bookDTO) {
		if (bookDTO == null) {
			return null;
		}
		Genre genre = new Genre(bookDTO.getGenre(), bookDTO.getGenreDescription());
		Author author = new Author(bookDTO.getAuthor(), bookDTO.getAuthorDescription());
		return new Book(bookDTO.getIsbn(),bookDTO.getCount(),author,bookDTO.getName(),bookDTO.getAuthorDescription(),
				genre);
	}
	
	public List<BookDTO> listToDTO(List<Book> entityList) {
		return entityList.stream().map(this::bookToDTO).collect(Collectors.toList());
	}
}
