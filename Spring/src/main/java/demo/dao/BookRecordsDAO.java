package demo.dao;

import demo.entities.Book;
import demo.entities.BooksActivity;
import demo.repositories.BookRecordsRepository;
import demo.repositories.UserRepository;
import demo.enums.Status;
import java.time.LocalDate;
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
			BooksActivity activity = new BooksActivity(user.findUserByName(userName), books.getBook(bookId), Status.TAKEN,
					LocalDate.now(), LocalDate.now().plusDays(20));
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
		BooksActivity temp = bookRecordsRepository.findFirstByBookIsbnAndStatusAndUserId(books.getBook(bookId).getIsbn(), Status.TAKEN, user.findUserByName(username).getId());
		temp.setStatus(Status.RETURNED);
		books.increaseCountBy1(bookId);
		bookRecordsRepository.save(temp);
		return books.getBook(bookId);
	}

	public List<BooksActivity> getAllUserRecords(String username){
		if (userRepository.findByUsername(username) == null) {
			return null;
		}
		return bookRecordsRepository.findByUserId(userRepository.findByUsername(username).getId());
	}
	
	public Map<String, List<String>> booksUsedByUser(String username) {
		if (userRepository.findByUsername(username) == null) {
			return null;
		}
		Map<String, List<String>> books = new HashMap<>();
		List<BooksActivity> records = bookRecordsRepository.findByUserId(userRepository.findByUsername(username).getId());
		if (records == null || records.isEmpty()) {
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
	
	public Set<String> getUsersByBook(String bookId) {
		if (books.getBook(bookId) != null) {
			Set<String> usernames = new HashSet<>();
			Set<BooksActivity> records = bookRecordsRepository.findByBookIsbnAndStatus(books.getBook(bookId).getIsbn(), Status.TAKEN);
			for (BooksActivity record : records) {
				usernames.add(record.getUser().getUsername());
			}
			return usernames;
		}
		return null;
	}

	public List<BooksActivity> getAllActivity(){
		return bookRecordsRepository.findAll();
	}

	public List<BooksActivity> getReadLogs(LocalDate date){
		return bookRecordsRepository.findByDate(date);
	}
	
	private List<String> loadHistory(String username) {
		return userRepository.findByUsername(username).getUserHistoryOfDeletedBooks();
	}
}

