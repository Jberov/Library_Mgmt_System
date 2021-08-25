package tests.integrationTests;

import demo.LibraryApplication;
import demo.entities.Books;
import demo.entities.BooksActivity;
import demo.entities.Status;
import demo.entities.Users;
import demo.repositories.BookRecordsRepository;
import demo.repositories.BookRepository;
import demo.repositories.UserRepository;
import org.junit.Before;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockServletContext;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;


import javax.servlet.ServletContext;



import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = { LibraryApplication.class })
@WebAppConfiguration(value = "src/main/java")
public class IntegrationTest {
    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    BookRepository bookRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    BookRecordsRepository bookRecordsRepository;

    private MockMvc mockMvc;

    @Before
    public void setup() {
        Books book = new Books("978-06-79826-62-9",3,"Roald Dahl","Matilda","Desc",true);
        bookRepository.save(book);
        Users user = new Users("A");
        userRepository.save(user);
        bookRecordsRepository.save(new BooksActivity(user,book,Status.TAKEN));
    }

    @BeforeEach
    public void setMockMvc(){
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
    public void givenGetUser_whenMockMVC_thenVerifyResponse() throws Exception{
        this.mockMvc.perform(get("/api/v1/users/info/JBaller")).andDo(print())
                .andExpect(status().is2xxSuccessful());
    }

    @Test
    public void givenAddURI_whenMockMVC_thenVerifyResponse() throws Exception {
        this.mockMvc.perform(post("/api/v1/book")).andDo(print())
                .andExpect(status().is4xxClientError());
                //.andExpect(content().string("Success"));
    }

    @Test
    public void givenGetBookURI_whenMockMVC_thenVerifyResponse() throws Exception {
        this.mockMvc.perform(get("/api/v1/book/0")).andDo(print())
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void givenGetBookURI_whenMockMVC_thenVerifyResponseNoParams() throws Exception {
        mockMvc.perform(get("/api/v1/book")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());
                //.andExpect(result -> Assertions.assertTrue(result.getResolvedException() instanceof ResponseStatusException));
                //.andExpect(result -> Assertions.assertEquals("Invalid input", Objects.requireNonNull(result.getResolvedException()).getMessage()));
    }

    @Test
    public void givenGetBookURI_whenMockMVC_thenVerifyResponseNoBook() throws Exception {
        mockMvc.perform(get("/api/v1/book/978-06-79826-62-9")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());
        //.andExpect(result -> Assertions.assertTrue(result.getResolvedException() instanceof ResponseStatusException));
        //.andExpect(result -> Assertions.assertEquals("Invalid input", Objects.requireNonNull(result.getResolvedException()).getMessage()));
    }

    @Test
    public void givenDeleteURI_whenMockMVC_thenVerifyResponse() throws Exception {
        this.mockMvc.perform(delete("/admin/books/delete/Matilda")).andDo(print())
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void givenDeleteURI_whenMockMVC_thenVerifyResponseNoParam() throws Exception {
        this.mockMvc.perform(delete("/api/v1/book/delete")).andDo(print())
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void givenGetUsersByBook_whenMockMVC_thenVerifyResponse() throws Exception{
        this.mockMvc.perform(get("/api/v1/users/byBook/978-06-79826-62-9")).andDo(print())
                .andExpect(status().is4xxClientError());
    }



    @Test
    public void givenGetBooks_whenMockMVC_thenVerifyResponse() throws Exception{
        this.mockMvc.perform(get("/api/v1/books")).andDo(print())
                .andExpect(status().is2xxSuccessful());
    }

    @Test
    public void givenLease_whenMockMVC_thenVerifyResponse() throws Exception{
        this.mockMvc.perform(patch("/users/lease/978-06-79826-62-9&A")).andDo(print())
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void givenLease_whenMockMVC_thenVerifyResponseNoUser() throws Exception{
        this.mockMvc.perform(patch("/users/lease/0&A")).andDo(print())
                .andExpect(status().is4xxClientError());
                //.andExpect(content().string("No such book"));
    }

    @Test
    public void givenReturn_whenMockMVC_thenVerifyResponse() throws Exception{
        this.mockMvc.perform(patch("/api/v1/books/return/978-06-79826-62-9&A")).andDo(print())
                .andExpect(status().is4xxClientError());
    }

    @Test
    @WithMockUser
    public void givenReturn_whenMockMVC_thenVerifyResponseInvalidUser() throws Exception{
        this.mockMvc.perform(patch("/api/v1/books/return/0&A")).andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    public void givenHistory_whenMockMVC_thenVerifyResponse() throws Exception{
        this.mockMvc.perform(get("/users/history?username=A")).andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    public void givenHistory_whenMockMVC_thenVerifyResponseNoUser() throws Exception{
        this.mockMvc.perform(get("/api/v1/users/history/A")).andDo(print())
                .andExpect(status().is2xxSuccessful());
    }

    @Test
    public void givenHistory_whenMockMVC_thenVerifyResponseNewUser() throws Exception{
        this.mockMvc.perform(get("/api/v1/users/history/5")).andDo(print())
                .andExpect(status().is2xxSuccessful());
    }
}
