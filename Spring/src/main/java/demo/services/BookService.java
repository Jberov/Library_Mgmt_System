package demo.services;

import demo.dao.BookRecordsDAO;
import demo.dao.BooksDAOImpl;
import demo.dto.BookDTO;
import demo.mappers.BookMapper;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BookService {
	private final BooksDAOImpl booksDAO;
	private final BookRecordsDAO bookRecordsDAO;
	private final BookMapper bookMapper;
	
	@Autowired
	public BookService(BooksDAOImpl booksDAO, BookRecordsDAO bookRecordsDAO, BookMapper bookMapper) {
		this.booksDAO = booksDAO;
		this.bookRecordsDAO = bookRecordsDAO;
		this.bookMapper = bookMapper;
	}
	
	public List<BookDTO> getAllBooks() {
		return bookMapper.listToDTO(booksDAO.getAllBooks());
	}
	
	public BookDTO addBook(BookDTO bookDTO) {
		if (booksDAO.getBook(bookDTO.getIsbn()) != null) {
			if (!booksDAO.getBook(bookDTO.getIsbn()).getName().equals(bookDTO.getName())) {
				return null;
			}
			return bookMapper.bookToDTO(booksDAO.increaseCount(bookDTO.getIsbn(), bookDTO.getCount()));
		}
		System.out.println("Here");
		return bookMapper.bookToDTO(booksDAO.addBook(bookMapper.bookToEntity(bookDTO)));
	}
	
	public BookDTO deleteBook(String name) {
		if ((booksDAO.getBookByName(name) == null) || (bookRecordsDAO.userHistoryExists(name))) {
			return null;
		}
		return bookMapper.bookToDTO(booksDAO.deleteBook(name));
	}
	
	public BookDTO getBookById(String isbn) {
		return bookMapper.bookToDTO(booksDAO.getBook(isbn));
	}

	public BookDTO getBookByName(String name) {
		return bookMapper.bookToDTO(booksDAO.getBookByName(name));
	}
}

