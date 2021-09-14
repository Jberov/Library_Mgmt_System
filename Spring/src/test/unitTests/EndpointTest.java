package unitTests;

import com.sap.cloud.security.xsuaa.XsuaaServiceConfiguration;
import demo.LibraryApplication;
import demo.entities.Book;
import demo.entities.BooksActivity;
import demo.entities.User;
import demo.repositories.BookRecordsRepository;
import demo.repositories.BookRepository;
import demo.repositories.UserRepository;
import demo.status.Status;
import org.junit.Before;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockServletContext;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import javax.servlet.ServletContext;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = {
        LibraryApplication.class,
        XsuaaServiceConfiguration.class})

public class EndpointTest {
    private final BookRepository bookRepository;
    private final UserRepository userRepository;
    private final BookRecordsRepository bookRecordsRepository;
    private final WebApplicationContext webApplicationContext;
    private MockMvc mockMvc;
    private static final String urlBase = "api/v1/books";
    private static String userURL;
    
    @Autowired
    public EndpointTest(BookRepository bookRepository, UserRepository userRepository, BookRecordsRepository bookRecordsRepository, WebApplicationContext webApplicationContext) {
        this.bookRepository = bookRepository;
        this.userRepository = userRepository;
        this.bookRecordsRepository = bookRecordsRepository;
        this.webApplicationContext = webApplicationContext;
    }
    
    @Before
    public void setup() {
        Book book = new Book("978-06-79826-62-9", 3, "Roald Dahl", "Matilda", "Desc", true);
        bookRepository.save(book);
        User user = new User("A");
        userRepository.save(user);
        bookRecordsRepository.save(new BooksActivity(user, book, Status.TAKEN));
    }

    @BeforeEach
    public void setMockMvc() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.webApplicationContext).build();
    }

    @Test
    public void givenWac_whenServletContext_thenItProvidesController() {
        ServletContext servletContext = webApplicationContext.getServletContext();
        Assertions.assertNotNull(servletContext);
        Assertions.assertTrue(servletContext instanceof MockServletContext);
        Assertions.assertNotNull(webApplicationContext.getBean("addBookCommand"));
        Assertions.assertNotNull(webApplicationContext.getBean("removeBookCommand"));
        Assertions.assertNotNull(webApplicationContext.getBean("getBookCommand"));
        Assertions.assertNotNull(webApplicationContext.getBean("getBookUsedAtTheMomentCommand"));
        Assertions.assertNotNull(webApplicationContext.getBean("getUserCommand"));
        Assertions.assertNotNull(webApplicationContext.getBean("leaseBookCommand"));
        Assertions.assertNotNull(webApplicationContext.getBean("listAllBooksCommand"));
        Assertions.assertNotNull(webApplicationContext.getBean("returnBookCommand"));
        Assertions.assertNotNull(webApplicationContext.getBean("userHistoryCommand"));
    }

    @Test
    public void givenGetUser_whenMockMVC_thenVerifyResponse() throws Exception {
        userURL = "/api/v1/users/info/A";
        this.mockMvc.perform(get(userURL)).andDo(print())
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void givenAddURI_whenMockMVC_thenVerifyResponse() throws Exception {
        this.mockMvc.perform(post(urlBase)).andDo(print())
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void givenGetBookURI_whenMockMVC_thenVerifyResponse() throws Exception {
        this.mockMvc.perform(get(urlBase + "/978-06-79826-62-9")).andDo(print())
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void givenGetBookURI_whenMockMVC_thenVerifyResponseNoParams() throws Exception {
        mockMvc.perform(get(urlBase)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());

    }

    @Test
    public void givenGetBookURI_whenMockMVC_thenVerifyResponseNoBook() throws Exception {
        mockMvc.perform(get(urlBase + "/979-06-79826-62-9")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void givenDeleteURI_whenMockMVC_thenVerifyResponse() throws Exception {
        this.mockMvc.perform(delete(urlBase + "/978-06-79826-62-9")).andDo(print())
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void givenDeleteURI_whenMockMVC_thenVerifyResponseNoParam() throws Exception {
        this.mockMvc.perform(delete(urlBase)).andDo(print())
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void givenGetUsersByBook_whenMockMVC_thenVerifyResponse() throws Exception {
       userURL = "/api/v1/users/byBook/978-06-79826-62-9";
        this.mockMvc.perform(get(userURL)).andDo(print())
                .andExpect(status().is4xxClientError());
    }


    @Test
    public void givenGetBooks_whenMockMVC_thenVerifyResponse() throws Exception {
        this.mockMvc.perform(get(urlBase)).andDo(print())
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void givenLease_whenMockMVC_thenVerifyResponse() throws Exception {
        userURL = "/users/lease/978-06-79826-62-9";
        this.mockMvc.perform(patch(userURL)).andDo(print())
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void givenLease_whenMockMVC_thenVerifyResponseNoUser() throws Exception {
        userURL = "/users/lease/0";
        this.mockMvc.perform(patch(userURL)).andDo(print())
                .andExpect(status().is4xxClientError());
    }

    @Test
    @WithUserDetails(value = "A")
    public void givenReturn_whenMockMVC_thenVerifyResponse() throws Exception {
        userURL = "/api/v1/books/return/978-06-79826-62-9";
        this.mockMvc.perform(patch(userURL)).andDo(print())
                .andExpect(status().is4xxClientError());
    }

    @Test
    @WithMockUser
    public void givenHistory_whenMockMVC_thenVerifyResponse() throws Exception {
        userURL = "/users/history";
        this.mockMvc.perform(get(userURL)).andDo(print())
                .andExpect(status().isNotFound());
    }
    
}
