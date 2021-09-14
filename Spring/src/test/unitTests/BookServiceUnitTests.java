package unitTests;

import com.sap.cloud.security.xsuaa.XsuaaServiceConfiguration;
import demo.LibraryApplication;
import demo.dao.BookRecordsDAO;
import demo.dao.BooksDAOImpl;
import demo.dao.UserDAOImpl;
import demo.entities.Book;
import demo.services.BookService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.LinkedList;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {
        LibraryApplication.class,
        XsuaaServiceConfiguration.class})
public class BookServiceUnitTests {
    
    
    private static String url = "api/v1/books";
    private MockMvc mvc;
    @MockBean
    private BookService bookService;
    @MockBean
    private BooksDAOImpl booksDAO;
    @MockBean
    private UserDAOImpl userDAO;
    @MockBean
    private BookRecordsDAO bookRecordsDAO;
    private final WebApplicationContext webApplicationContext;
    
    @Autowired
    public BookServiceUnitTests(WebApplicationContext webApplicationContext) {
        this.webApplicationContext = webApplicationContext;
    }
    
    public static String getUrl() {
        return url;
    }
    
    public static void setUrl(String url) {
        BookServiceUnitTests.url = url;
    }
    
    @Before
    public void setUp() {
        mvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @Test
    public void addBook() throws Exception {
        mvc.perform(MockMvcRequestBuilders.post(url)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void getAllBooks() throws Exception {
        Book book = new Book("9780141301068", 3, "Roald Dahl", "Matilda", "Genius girl", true);
        LinkedList<Book> books = new LinkedList<>();
        books.add(book);
        BDDMockito.given(booksDAO.getAllBooks()).willReturn(books);
        mvc.perform(MockMvcRequestBuilders.get(url)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void deleteBook() throws Exception {
        mvc.perform(MockMvcRequestBuilders.delete(url + "/9780141301068")
                .contentType(MediaType.APPLICATION_JSON)).andExpect(status().is4xxClientError());
    }

    @Test
    public void getBookById() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get(url + "/9780141301068")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());
    }
}