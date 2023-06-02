package demo.dao;

import demo.entities.Book;
import demo.entities.BooksActivity;
import demo.repositories.BookRecordsRepository;
import demo.repositories.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.LinkedList;
import java.util.List;

@Service
public class BooksDAOImpl {
	private final BookRepository bookRepository;
	
	private final BookRecordsRepository bookRecordsRepository;
	
	private final UserDAOImpl userDAO;
	
	
	@Autowired
	public BooksDAOImpl(BookRepository bookRepository, BookRecordsRepository bookRecordsRepository, UserDAOImpl userDAO) {
		this.bookRepository = bookRepository;
		this.bookRecordsRepository = bookRecordsRepository;
		this.userDAO = userDAO;
	}
	
	public List<Book> getAllBooks() throws ResponseStatusException {
		return new LinkedList<>(bookRepository.findAll());
	}
	
	public Book addBook(Book book) {
		return bookRepository.save(book);
	}
	
	public Book deleteBook(String isbn) {
		Book book = getBook(isbn);
		cleanBookRecords(isbn);
		bookRepository.delete(book);
		return book;
	}
	
	public Book getBook(String isbn) {
		return bookRepository.findByIsbn(isbn);
	}

	public Book getBookByName(String name) {
		return bookRepository.findByName(name);
	}
	
	
	public boolean bookExistsByID(String isbn) {
		return bookRepository.findByIsbn(isbn) != null;
	}
	
	
	public void decreaseCount(String bookId) {
		Book temp = bookRepository.findByIsbn(bookId);
		temp.setCount(temp.getCount() - 1);
		bookRepository.save(temp);
	}
	
	public void increaseCountBy1(String bookId) {
		Book temp = bookRepository.findByIsbn(bookId);
		temp.setCount(temp.getCount() + 1);
		bookRepository.save(temp);
	}
	
	public Book increaseCount(String bookId, int count) {
		Book temp = bookRepository.findByIsbn(bookId);
		temp.setCount(temp.getCount() + count);
		bookRepository.save(temp);
		return temp;
	}
	
	public int getCount(String isbn) {
		return bookRepository.findByIsbn(isbn).getCount();
	}
	
	private void cleanBookRecords(String isbn){
		List<BooksActivity> records = bookRecordsRepository.findByBookIsbn(isbn);
		String bookString;
		for (BooksActivity record : records) {
			System.out.println(record.getBook().getName());
			bookString = record.getBook().getName() + ", " + record.getBook().getAuthor();
			userDAO.saveBookToHistory(record.getUser(), bookString);
			bookRecordsRepository.delete(record);
		}
	}
}

