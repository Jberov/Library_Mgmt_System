package unitTests;

import com.sap.cloud.security.xsuaa.XsuaaServiceConfiguration;
import demo.LibraryApplication;
import demo.dao.BookRecordsDAO;
import demo.entities.Book;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.mockito.BDDMockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {
		LibraryApplication.class,
		XsuaaServiceConfiguration.class})
public class BookActivityDAOTests {
	private static final String username = "";
	
	private static final String wrongIsbn = "978-0-09-923808-7";
	
	private static final Book book = new Book("978-0-09-959008-8",7,"Ювал Харари","Кратка история на Хомо Сапиенс","История");
	
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
		List<String> list = new ArrayList<>();
		BDDMockito.given(bookRecordsDAO.getUsersByBook(book.getIsbn())).willReturn(list);
		Assertions.assertEquals(bookRecordsDAO.getUsersByBook(book.getIsbn()), list);
	}
	
	@Test
	public void getUsersByNullBookTest() {
		List<String> list = new ArrayList<>();
		BDDMockito.given(bookRecordsDAO.getUsersByBook(wrongIsbn)).willReturn(list);
		Assertions.assertTrue(bookRecordsDAO.getUsersByBook(book.getIsbn()).isEmpty());
	}
}
