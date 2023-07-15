package unitTest;

import demo.LibraryApplication;
import demo.dao.BooksDAOImpl;
import demo.dao.UserDAOImpl;
import demo.entities.Author;
import demo.entities.Book;
import demo.entities.Genre;
import java.util.ArrayList;
import java.util.List;
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
public class BookDAOTest {

	private static final Book book = new Book("978-0-09-959008-8",7,new Author("Харари", "Еврейски публицист"),"Кратка история на Хомо Сапиенс","История", new Genre("Публицистика", "Описание"));
	
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
