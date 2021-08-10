package Library.demo;

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
@WebAppConfiguration(value = "src/main")
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
        Books book = new Books(3,"Roald Dahl","Matilda","Desc",true);
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
        Assertions.assertNotNull(webApplicationContext.getBean("addBookAdminCommand"));
        Assertions.assertNotNull(webApplicationContext.getBean("deleteBookAdminCommand"));
        Assertions.assertNotNull(webApplicationContext.getBean("getBookAdminCommand"));
        Assertions.assertNotNull(webApplicationContext.getBean("getBookUsedAtTheMomentCommand"));
        Assertions.assertNotNull(webApplicationContext.getBean("getUserCommand"));
        Assertions.assertNotNull(webApplicationContext.getBean("leaseBookCommand"));
        Assertions.assertNotNull(webApplicationContext.getBean("listAllBooksAdminCommand"));
        Assertions.assertNotNull(webApplicationContext.getBean("returnBookCommand"));
        Assertions.assertNotNull(webApplicationContext.getBean("userBooksCommand"));
    }

    @Test
    public void givenGetUser_whenMockMVC_thenVerifyResponse() throws Exception{
        this.mockMvc.perform(get("/admin/user/profile?name=JBaller")).andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    public void givenAddURI_whenMockMVC_thenVerifyResponse() throws Exception {
        this.mockMvc.perform(post("/admin/addBook?count_books=8&author=Dan Abnett&name=Master of Mankind&description=Desc")).andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string("Success"));
    }
    @Test
    public void givenGetBookURI_whenMockMVC_thenVerifyResponse() throws Exception {
        this.mockMvc.perform(get("/admin/getBook?isbn=0")).andDo(print())
                .andExpect(status().isOk());
    }
    @Test
    public void givenGetBookURI_whenMockMVC_thenVerifyResponseNoParams() throws Exception {
        mockMvc.perform(get("/admin/getBook")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
                //.andExpect(result -> Assertions.assertTrue(result.getResolvedException() instanceof ResponseStatusException));
                //.andExpect(result -> Assertions.assertEquals("Invalid input", Objects.requireNonNull(result.getResolvedException()).getMessage()));
    }
    @Test
    public void givenGetBookURI_whenMockMVC_thenVerifyResponseNoBook() throws Exception {
        mockMvc.perform(get("/admin/getBook?isbn=3452")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        //.andExpect(result -> Assertions.assertTrue(result.getResolvedException() instanceof ResponseStatusException));
        //.andExpect(result -> Assertions.assertEquals("Invalid input", Objects.requireNonNull(result.getResolvedException()).getMessage()));
    }



    @Test
    public void givenDeleteURI_whenMockMVC_thenVerifyResponse() throws Exception {
        this.mockMvc.perform(patch("/admin/books/delete?name=Master of Mankind")).andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string("Success"));
    }
    @Test
    public void givenDeleteURI_whenMockMVC_thenVerifyResponseNoParam() throws Exception {
        this.mockMvc.perform(patch("/admin/books/delete")).andDo(print())
                .andExpect(status().isOk());
    }
    @Test
    public void givenDeleteURI_whenMockMVC_thenVerifyResponseNoName() throws Exception {
        this.mockMvc.perform(patch("/admin/books/delete?name=")).andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string("No such book found"));
    }

    @Test
    public void givenGetUsersByBook_whenMockMVC_thenVerifyResponse() throws Exception{
        this.mockMvc.perform(get("/admin/book/users?isbn=1")).andDo(print())
                .andExpect(status().isOk());
    }
    @Test
    public void givenGetUsersByBook_whenMockMVC_thenVerifyResponseNoBook() throws Exception{
        this.mockMvc.perform(get("/admin/book/users?isbn=2341")).andDo(print())
                .andExpect(status().isOk());
    }
    @Test
    public void givenGetBooks_whenMockMVC_thenVerifyResponse() throws Exception{
        this.mockMvc.perform(get("/admin/books/all")).andDo(print())
                .andExpect(status().isNotFound());
    }
    @Test
    public void givenLease_whenMockMVC_thenVerifyResponse() throws Exception{
        this.mockMvc.perform(patch("/users/lease?isbn=1&username=A")).andDo(print())
                .andExpect(status().isOk());
    }
    @Test
    public void givenLease_whenMockMVC_thenVerifyResponseNoUser() throws Exception{
        this.mockMvc.perform(patch("/users/lease?isbn=0&username=A")).andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string("No such book"));
    }
    @Test
    public void givenReturn_whenMockMVC_thenVerifyResponse() throws Exception{
        this.mockMvc.perform(patch("/users/returnBook?isbn=1&username=A")).andDo(print())
                .andExpect(status().isOk());
    }
    @Test
    public void givenReturn_whenMockMVC_thenVerifyResponseInvalidUser() throws Exception{
        this.mockMvc.perform(patch("/users/returnBook?isbn=0&username=A")).andDo(print())
                .andExpect(status().isOk()).andExpect(content().string("No such user or book"));
    }
    @Test
    public void givenHistory_whenMockMVC_thenVerifyResponse() throws Exception{
        this.mockMvc.perform(get("/users/history?username=A")).andDo(print())
                .andExpect(status().isOk());
    }
    @Test
    public void givenHistory_whenMockMVC_thenVerifyResponseNoUser() throws Exception{
        this.mockMvc.perform(get("/users/history?username=D")).andDo(print())
                .andExpect(status().isOk());
    }
    @Test
    public void givenHistory_whenMockMVC_thenVerifyResponseBadInput() throws Exception{
        this.mockMvc.perform(get("/users/history?username=6")).andDo(print())
                .andExpect(status().isOk());
    }
}
