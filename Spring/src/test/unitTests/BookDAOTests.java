package unitTests;

import com.sap.cloud.security.xsuaa.XsuaaServiceConfiguration;
import demo.LibraryApplication;
import demo.dao.BooksDAOImpl;
import demo.dao.UserDAOImpl;
import demo.dto.BookDTO;
import demo.entities.Book;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.mockito.BDDMockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {
		LibraryApplication.class,
		XsuaaServiceConfiguration.class})
public class BookDAOTests {
	
	private static final Book book = new Book("978-0-09-959008-8",7,"Ювал Харари","Кратка история на Хомо Сапиенс","История");
	
	@MockBean
	private UserDAOImpl userDAO;
	
	@MockBean
	private BooksDAOImpl booksDAO;
	
	@Test
	public void getBooksListTest() {
		List<Book> list = new ArrayList<>();
		list.add(book);
		BDDMockito.given(booksDAO.getAllBooks()).willReturn(list);
		Assertions.assertEquals(booksDAO.getAllBooks(), list);
	}
	
	@Test
	public void getBookTest() {
		BDDMockito.given(booksDAO.getBook(book.getIsbn())).willReturn(book);
		Assertions.assertEquals(booksDAO.getBook(book.getIsbn()).getName(), book.getName());
	}
}
