package demo.mappers;

import demo.dto.BookDTO;
import demo.entities.Books;
import org.springframework.stereotype.Component;

import java.util.LinkedList;

@Component
public class BookMapper {
    public BookDTO bookToDTO(Books book) {
        if (book == null) {
            return null;
        }
        BookDTO bookDTO = new BookDTO();
        bookDTO.setIsbn(book.getIsbn());
        bookDTO.setCount(book.getCount());
        bookDTO.setAuthor(book.getAuthor());
        bookDTO.setName(book.getName());
        bookDTO.setDescription(book.getDescription());
        bookDTO.setExists(book.isExists());
        return bookDTO;
    }

    public Books bookToEntity(BookDTO bookDTO) {
        if (bookDTO == null) {
            return null;
        }
        Books books = new Books();
        books.setIsbn(bookDTO.getIsbn());
        books.setCount(books.getCount());
        books.setAuthor(bookDTO.getAuthor());
        books.setName(bookDTO.getName());
        books.setDescription(bookDTO.getDescription());
        books.setExists(bookDTO.isExists());
        return books;
    }

    public LinkedList<BookDTO> linkedListToDTO(LinkedList<Books> entityList) {
        LinkedList<BookDTO> dtoLinkedList = new LinkedList<>();
        for(Books book : entityList){
            dtoLinkedList.add(bookToDTO(book));
        }
        return dtoLinkedList;
    }
}
