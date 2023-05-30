package demo.mappers;

import demo.dto.BookDTO;
import demo.entities.Book;
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
		bookDTO.setAuthor(book.getAuthor());
		bookDTO.setName(book.getName());
		bookDTO.setDescription(book.getDescription());
		bookDTO.setGenre(book.getGenre());
		return bookDTO;
	}
	
	public Book bookToEntity(BookDTO bookDTO) {
		if (bookDTO == null) {
			return null;
		}
		return new Book(bookDTO.getIsbn(),bookDTO.getCount(),bookDTO.getAuthor(),bookDTO.getName(),bookDTO.getDescription(),
				bookDTO.getGenre());
	}
	
	public List<BookDTO> listToDTO(List<Book> entityList) {
		return entityList.stream().map(this::bookToDTO).collect(Collectors.toList());
	}
}
