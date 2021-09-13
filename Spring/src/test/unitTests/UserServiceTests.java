package unitTests;

import com.sap.cloud.security.xsuaa.XsuaaServiceConfiguration;
import demo.LibraryApplication;
import demo.dao.BookRecordsDAO;
import demo.dao.BooksDAOImpl;
import demo.dao.UserDAOImpl;
import demo.dto.BookDTO;
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

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {
        LibraryApplication.class,
        XsuaaServiceConfiguration.class})
public class UserServiceTests {

    private MockMvc mvc;

    @Autowired
    private WebApplicationContext webApplicationContext;

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


    @Before
    public void setUp() {
        mvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @Test
    public void getUserTest() throws Exception {
        User user = new User("yordan.berov@sap.com");
        BDDMockito.given(userService.getUser("yordan.berov@sap.com")).willReturn(userMapper.userToDTO(user));
        mvc.perform(MockMvcRequestBuilders.get("/api/v1/users/info").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful());

    }

    @Test
    public void leaseBook() throws Exception {
        Book book = new Book("978-17-82065-33-9", 3, "Author", "Name", "Desc", true);
        User user = new User("yordan.berov@sap.com");
        BDDMockito.given(userService.leaseBook(book.getIsbn(), user.getName())).willReturn(bookMapper.bookToDTO(book));
        mvc.perform(MockMvcRequestBuilders.patch("/api/v1/books/rental/978-17-82065-33-9").contentType(MediaType.TEXT_PLAIN_VALUE))
                .andExpect(status().is2xxSuccessful())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    public void returnBook() throws Exception {
        Book book = new Book("978-17-82065-33-9", 3, "Author", "Name", "Desc", true);
        User user = new User("JBaller");
        BDDMockito.given(userService.returnBook(book.getIsbn(), user.getName())).willReturn(bookMapper.bookToDTO(book));
        mvc.perform(MockMvcRequestBuilders.patch("/api/v1/books/return/978-06-79866-68-8&JBaller").contentType(MediaType.TEXT_PLAIN_VALUE))
                .andExpect(status().is2xxSuccessful());
        //.andExpect(content().string("Book successfully returned"));


    }

    @Test
    public void userUsedBooksTest() throws Exception {
        LinkedList<BookDTO> takenList = new LinkedList<>();
        LinkedList<BookDTO> returnedList = new LinkedList<>();
        HashMap<String, LinkedList<BookDTO>> booksList = new HashMap<>();
        booksList.put("Currently taken books by user:", takenList);
        booksList.put("Already returned books by user:", returnedList);
        Book book = new Book("978-17-82065-33-9", 3, "Author", "Name", "Desc", true);
        User user = new User("JBaller");
        takenList.add(bookMapper.bookToDTO(book));
        BDDMockito.given(userService.userUsedBooks(user.getName())).willReturn(booksList);
        mvc.perform(MockMvcRequestBuilders.get("/api/v1/users/history/JBaller").contentType(MediaType.TEXT_PLAIN_VALUE))
                .andExpect(status().is2xxSuccessful());
    }

    @Test
    public void getUsersByBookTest() throws Exception {
        LinkedList<String> userList = new LinkedList<>();
        Book book = new Book("978-17-82065-33-9", 3, "Author", "Name", "Desc", true);
        User user = new User("JBaller");
        userList.add(user.getName());
        BDDMockito.given(userService.getUsersByBook(book.getIsbn())).willReturn(userList);
        mvc.perform(MockMvcRequestBuilders.get("/api/v1/users/byBook/978-17-82065-33-9").contentType(MediaType.TEXT_PLAIN_VALUE))
                .andExpect(status().is2xxSuccessful());
    }
}
