package unitTest;

import com.sap.cloud.security.xsuaa.XsuaaServiceConfiguration;
import demo.LibraryApplication;
import demo.repositories.BookRecordsRepository;
import demo.repositories.BookRepository;
import demo.repositories.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockServletContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
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

@ActiveProfiles("test")
@ContextConfiguration(classes = LibraryApplication.class)
public class EndpointTest {
    private final WebApplicationContext webApplicationContext;
    private static final String jwt = "";
    private MockMvc mockMvc;
    private static final String urlBase = "/api/v1/books";
    private static String userURL;
    
    @Autowired
    public EndpointTest(BookRepository bookRepository, UserRepository userRepository, BookRecordsRepository bookRecordsRepository, WebApplicationContext webApplicationContext) {
        this.webApplicationContext = webApplicationContext;
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
        
        this.mockMvc.perform(get(userURL).header("Bearer", jwt)).andDo(print())
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void givenAddURI_whenMockMVC_thenVerifyResponse() throws Exception {
        this.mockMvc.perform(post(urlBase).header("Bearer", jwt)).andDo(print())
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void givenGetBookURI_whenMockMVC_thenVerifyResponse() throws Exception {
        this.mockMvc.perform(get(urlBase + "/978-06-79826-62-9").header("Bearer", jwt)).andDo(print())
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void givenGetBookURI_whenMockMVC_thenVerifyResponseNoParams() throws Exception {
        mockMvc.perform(get(urlBase).header("Bearer", jwt)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());

    }

    @Test
    public void givenGetBookURI_whenMockMVC_thenVerifyResponseNoBook() throws Exception {
        mockMvc.perform(get(urlBase + "/979-06-79826-62-9").header("Bearer", jwt)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void givenDeleteURI_whenMockMVC_thenVerifyResponse() throws Exception {
        this.mockMvc.perform(delete(urlBase + "/978-06-79826-62-9").header("Bearer", jwt)).andDo(print())
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void givenDeleteURI_whenMockMVC_thenVerifyResponseNoParam() throws Exception {
        this.mockMvc.perform(delete(urlBase).header("Bearer", jwt)).andDo(print())
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void givenGetUsersByBook_whenMockMVC_thenVerifyResponse() throws Exception {
       userURL = "/api/v1/users/byBook/978-06-79826-62-9";
        this.mockMvc.perform(get(userURL).header("Bearer", jwt))
                .andExpect(status().is4xxClientError());
    }


    @Test
    public void givenGetBooks_whenMockMVC_thenVerifyResponse() throws Exception {
        this.mockMvc.perform(get(urlBase).header("Bearer", jwt)).andDo(print())
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void givenLease_whenMockMVC_thenVerifyResponse() throws Exception {
        userURL = "/api/v1/books/rental/978-06-79826-62-9";
        this.mockMvc.perform(patch(userURL).header("Bearer", jwt)).andDo(print())
                .andExpect(status().is5xxServerError());
    }

    @Test
    public void givenLease_whenMockMVC_thenVerifyResponseNoUser() throws Exception {
        userURL = "/api/v1/books/rental/978-06-79826-62-9";
        this.mockMvc.perform(patch(userURL).header("Bearer", jwt)).andDo(print())
                .andExpect(status().is5xxServerError());
    }

    @Test
    public void givenReturn_whenMockMVC_thenVerifyResponse() throws Exception {
        userURL = "/api/v1/books/return/978-06-79826-62-9";
        this.mockMvc.perform(patch(userURL).header("Bearer", jwt)).andDo(print())
                .andExpect(status().is4xxClientError());
    }
    
}
