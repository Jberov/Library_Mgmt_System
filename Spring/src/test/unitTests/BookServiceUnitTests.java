package unitTests;

import com.sap.cloud.security.xsuaa.XsuaaServiceConfiguration;
import demo.LibraryApplication;
import demo.dao.BookRecordsDAO;
import demo.dao.BooksDAOImpl;
import demo.dao.UserDAOImpl;
import demo.dto.BookDTO;
import demo.services.BookService;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.mockito.BDDMockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import java.util.LinkedList;
import java.util.List;


@RunWith(SpringRunner.class)
@SpringBootTest(classes = {
        LibraryApplication.class,
        XsuaaServiceConfiguration.class})
public class BookServiceUnitTests {
    
    private static final BookDTO bookDTO = new BookDTO("978-0-09-959008-8",7,"Ювал Харари","Кратка история на Хомо Сапиенс","История");
    @MockBean
    private BookService bookService;
    @MockBean
    private BooksDAOImpl booksDAO;
    @MockBean
    private UserDAOImpl userDAO;
    @MockBean
    private BookRecordsDAO bookRecordsDAO;

    @Test
    public void addBook() {
        BDDMockito.given(bookService.addBook(bookDTO)).willReturn(bookDTO);
        Assertions.assertEquals(bookDTO.getCount(), 7);
    }

    @Test
    public void getAllBooks() {
        List<BookDTO> books = new LinkedList<>();
        BDDMockito.given(bookService.getAllBooks()).willReturn(books);
        Assertions.assertEquals(bookService.getAllBooks(), books);
    }

    @Test
    public void deleteBook() {
        BDDMockito.given(bookService.deleteBook(bookDTO.getIsbn())).willReturn(bookDTO);
        Assertions.assertEquals(bookDTO.getAuthor(),"Ювал Харари");
        
    }
    
    @Test
    public void deleteNonExistingBook() {
        BDDMockito.given(bookService.deleteBook("978-1-06-954008-8")).willReturn(bookDTO);
        Assertions.assertNotNull(bookService.deleteBook("978-1-06-954008-8"),"No such book exists or not all users have returned it yet.");
        
    }

    @Test
    public void getBookById() {
        BDDMockito.given(bookService.getBookById(bookDTO.getIsbn())).willReturn(bookDTO);
        Assertions.assertEquals(bookDTO.getAuthor(),"Ювал Харари");
    }
    
    @Test
    public void getNonExistingBook() {
        BDDMockito.given(bookService.getBookById("978-1-06-954008-8")).willReturn(bookDTO);
        Assertions.assertNotNull(bookService.getBookById("978-1-06-954008-8"),"No such book found");
        
    }
}