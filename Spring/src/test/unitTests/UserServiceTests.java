package unitTests;

import com.sap.cloud.security.xsuaa.XsuaaServiceConfiguration;
import demo.LibraryApplication;
import demo.dao.BookRecordsDAO;
import demo.dao.BooksDAOImpl;
import demo.dao.UserDAOImpl;
import demo.dto.BookDTO;
import demo.dto.UserDTO;
import demo.entities.Book;
import demo.entities.User;
import demo.mappers.BookMapper;
import demo.mappers.UserMapper;
import demo.services.UserService;
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
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {
        LibraryApplication.class,
        XsuaaServiceConfiguration.class})
public class UserServiceTests {
    private static final String username = "";
    private static String url;
    private MockMvc mvc;
    private final WebApplicationContext webApplicationContext;
    @MockBean
    private UserService userService;
    @MockBean
    private BooksDAOImpl booksDAO;
    @MockBean
    private UserDAOImpl userDAO;
    @MockBean
    private BookRecordsDAO bookRecordsDAO;
    @MockBean
    private BookMapper bookMapper;
    @MockBean
    private UserMapper userMapper;
    
    @Autowired
    public UserServiceTests(WebApplicationContext webApplicationContext) {
        this.webApplicationContext = webApplicationContext;
    }
    
    @Before
    public void setUp() {
        mvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @Test
    public void getUserTest() throws Exception {
        url = "/api/v1/users/info";
        User user = new User(username);
        BDDMockito.given(userService.getUser(username)).willReturn(userMapper.userToDTO(user));
        mvc.perform(MockMvcRequestBuilders.get(url).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful());
    }

    @Test
    public void leaseBook() throws Exception {
        Book book = new Book("978-17-82065-33-9", 3, "Author", "Name", "Desc", true);
        User user = new User(username);
        url = "/api/v1/books/rental/978-17-82065-33-9";
        BDDMockito.given(userService.leaseBook(book.getIsbn(), user.getName())).willReturn(bookMapper.bookToDTO(book));
        mvc.perform(MockMvcRequestBuilders.patch(url).contentType(MediaType.TEXT_PLAIN_VALUE))
                .andExpect(status().is2xxSuccessful())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    public void returnBook() throws Exception {
        Book book = new Book("978-17-82065-33-9", 3, "Author", "Name", "Desc", true);
        User user = new User(username);
        url = "/api/v1/books/return/978-06-79866-68-8";
        BDDMockito.given(userService.returnBook(book.getIsbn(), user.getName())).willReturn(bookMapper.bookToDTO(book));
        mvc.perform(MockMvcRequestBuilders.patch(url).contentType(MediaType.TEXT_PLAIN_VALUE))
                .andExpect(status().is2xxSuccessful());
    }

    @Test
    public void userUsedBooksTest() throws Exception {
        List<BookDTO> takenList = new LinkedList<>();
        List<BookDTO> returnedList = new LinkedList<>();
        Map<String, List<BookDTO>> booksList = new HashMap<>();
        booksList.put("Currently taken books by user:", takenList);
        booksList.put("Already returned books by user:", returnedList);
        Book book = new Book("978-17-82065-33-9", 3, "Author", "Name", "Desc", true);
        UserDTO user = new UserDTO(username);
        takenList.add(bookMapper.bookToDTO(book));
        BDDMockito.given(userService.userUsedBooks(user.getName())).willReturn(booksList);
        url = "/api/v1/users/history/JBaller";
        mvc.perform(MockMvcRequestBuilders.get(url).contentType(MediaType.TEXT_PLAIN_VALUE))
                .andExpect(status().is2xxSuccessful());
    }

    @Test
    public void getUsersByBookTest() throws Exception {
        LinkedList<String> userList = new LinkedList<>();
        Book book = new Book("978-17-82065-33-9", 3, "Author", "Name", "Desc", true);
        User user = new User(username);
        userList.add(user.getName());
        BDDMockito.given(userService.getUsersByBook(book.getIsbn())).willReturn(userList);
        url = "/api/v1/users/byBook/978-17-82065-33-9";
        mvc.perform(MockMvcRequestBuilders.get(url).contentType(MediaType.TEXT_PLAIN_VALUE))
                .andExpect(status().is2xxSuccessful());
    }
}
