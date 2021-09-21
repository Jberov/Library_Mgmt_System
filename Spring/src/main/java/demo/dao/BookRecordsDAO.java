package demo.dao;

import demo.entities.Book;
import demo.entities.BooksActivity;
import demo.repositories.BookRecordsRepository;
import demo.repositories.UserRepository;
import demo.status.Status;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class BookRecordsDAO {
	private final BookRecordsRepository bookRecordsRepository;
	private final BooksDAOImpl books;
	private final UserDAOImpl user;
	private final UserRepository userRepository;
	
	@Autowired
	public BookRecordsDAO(BookRecordsRepository bookRecordsRepository, BooksDAOImpl books, UserDAOImpl user, UserRepository userRepository) {
		this.bookRecordsRepository = bookRecordsRepository;
		this.books = books;
		this.user = user;
		this.userRepository = userRepository;
	}
	
	public Book leaseBook(String bookId, String userName) {
		if (books.getBook(bookId) != null) {
			BooksActivity activity = new BooksActivity(user.findUserByName(userName), books.getBook(bookId), Status.TAKEN);
			books.decreaseCount(bookId);
			bookRecordsRepository.save(activity);
			return books.getBook(bookId);
		}
		return null;
	}
	
	public boolean checkIfUserHasTakenBook(String isbn, String username) {
		if (getUsersByBook(isbn) == null) {
			return false;
		}
		for (String name : getUsersByBook(isbn)) {
			if (name.equals(username)) {
				return true;
			}
		}
		return false;
	}
	
	public boolean userHistoryExists(String isbn) {
		return bookRecordsRepository.existsByBookIsbnAndStatus(isbn, Status.TAKEN);
	}
	
	public Book returnBook(String bookId, String username) {
		new BooksActivity();
		BooksActivity temp;
		temp = bookRecordsRepository.findFirstByBookIsbnAndStatusAndUserId(books.getBook(bookId).getIsbn(), Status.TAKEN, user.findUserByName(username).getId());
		temp.setStatus(Status.RETURNED);
		books.increaseCountBy1(bookId);
		bookRecordsRepository.save(temp);
		return books.getBook(bookId);
	}
	
	public Map<String, List<String>> booksUsedByUser(String username) {
		if (userRepository.findByName(username) == null) {
			return null;
		}
		Map<String, List<String>> books = new HashMap<>();
		List<BooksActivity> records = bookRecordsRepository.findByUserId(userRepository.findByName(username).getId());
		if (records == null) {
			return null;
		}
		List<String> takenBooks = new ArrayList<>();
		List<String> returnedBooks = loadHistory(username);
		books.put("Currently taken books by user:", takenBooks);
		books.put("Already returned books by user:", returnedBooks);
		for (BooksActivity record : records) {
			String bookHistory;
			if (record.getStatus().equals(Status.TAKEN)) {
				bookHistory = record.getBook().getName() + ", " + record.getBook().getAuthor();
				books.get("Currently taken books by user:").add(bookHistory);
			} else {
				bookHistory = record.getBook().getName() + ", " + record.getBook().getAuthor();
				books.get("Already returned books by user:").add(bookHistory);
			}
		}
		return books;
	}
	
	public List<String> getUsersByBook(String bookId) {
		if (books.getBook(bookId) != null) {
			List<String> usernames = new LinkedList<>();
			List<BooksActivity> records = bookRecordsRepository.findByBookIsbnAndStatus(books.getBook(bookId).getIsbn(), Status.TAKEN);
			for (BooksActivity record : records) {
				usernames.add(record.getUser().getName());
			}
			return usernames;
		}
		return null;
	}
	
	private List<String> loadHistory(String username) {
		return userRepository.findByName(username).getUserHistoryOfDeletedBooks();
	}
}

