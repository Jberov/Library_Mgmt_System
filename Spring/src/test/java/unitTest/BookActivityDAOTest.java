package unitTest;

import demo.LibraryApplication;
import demo.dao.BookRecordsDAO;
import demo.entities.Book;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ActiveProfiles("test")
@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = {
		LibraryApplication.class})
@ContextConfiguration(classes = LibraryApplication.class)
public class BookActivityDAOTest {
	private static final String username = "yordan.berov@sap.com";
	
	private static final String wrongIsbn = "978-0-09-923808-7";
	
	private static final Book book = new Book("978-0-09-959008-8",7,"Ювал Харари","Кратка история на Хомо Сапиенс","История", "Estetika");
	
	@MockBean
	private BookRecordsDAO bookRecordsDAO;
	
	@Test
	public void leaseBookTest() {
		BDDMockito.given(bookRecordsDAO.leaseBook(book.getIsbn(),username)).willReturn(book);
		Assertions.assertNotNull(bookRecordsDAO.leaseBook(book.getIsbn(),username), "Successful lease of the book.");
		Assertions.assertEquals(7, bookRecordsDAO.leaseBook(book.getIsbn(),username).getCount());
	}
	
	@Test
	public void leaseNullTest() {
		BDDMockito.given(bookRecordsDAO.leaseBook(wrongIsbn,username)).willReturn(null);
		Assertions.assertNull(bookRecordsDAO.leaseBook(wrongIsbn,username), "Book does not exist or is not available");
	}
	
	@Test
	public void returnBookTest() {
		BDDMockito.given(bookRecordsDAO.returnBook(book.getIsbn(),username)).willReturn(book);
		Assertions.assertEquals(bookRecordsDAO.returnBook(book.getIsbn(),username).getCount(), 7);
		Assertions.assertNotNull(bookRecordsDAO.returnBook(book.getIsbn(),username), "Book successfully returned");
	}
	
	@Test
	public void returnNullTest() {
		BDDMockito.given(bookRecordsDAO.returnBook(wrongIsbn,username)).willReturn(null);
		Assertions.assertNull(bookRecordsDAO.returnBook(wrongIsbn,username), "Book does not exist or is not available");
	}
	
	@Test
	public void getBooksByUserTest() {
		Map<String, List<String>> map = new HashMap<>();
		BDDMockito.given(bookRecordsDAO.booksUsedByUser(username)).willReturn(map);
		Assertions.assertEquals(bookRecordsDAO.booksUsedByUser(username), map);
	}
	
	@Test
	public void getUsersByBookTest() {
		Set<String> set = new HashSet<>();
		BDDMockito.given(bookRecordsDAO.getUsersByBook(book.getIsbn())).willReturn(set);
		Assertions.assertEquals(bookRecordsDAO.getUsersByBook(book.getIsbn()), set);
	}
	
	@Test
	public void getUsersByNullBookTest() {
		Set<String> list = new HashSet<>();
		BDDMockito.given(bookRecordsDAO.getUsersByBook(wrongIsbn)).willReturn(list);
		Assertions.assertTrue(bookRecordsDAO.getUsersByBook(book.getIsbn()).isEmpty());
	}
}
