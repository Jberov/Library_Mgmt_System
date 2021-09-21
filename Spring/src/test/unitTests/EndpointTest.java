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
    private static final String jwt = "eyJhbGciOiJSUzI1NiIsImprdSI6Imh0dHBzOi8vbGlicmFyeS1tYW5hZ21lbnQuYXV0aGVudGljYXRpb24uc2FwLmhhbmEub25kZW1hbmQuY29tL3Rva2VuX2tleXMiLCJraWQiOiJkZWZhdWx0LWp3dC1rZXktLTUyNzk1MjQyOCIsInR5cCI6IkpXVCJ9.eyJqdGkiOiJjMGNjMDBlYzIwZmE0MGUzYTI1YzMzZDBlYmVlNGY3ZCIsImV4dF9hdHRyIjp7ImVuaGFuY2VyIjoiWFNVQUEiLCJzdWJhY2NvdW50aWQiOiI5MTg4MDYxNS1hYzU0LTQ4OTgtYTk3NC1mZWE0NmNiOGU2NDIiLCJ6ZG4iOiJsaWJyYXJ5LW1hbmFnbWVudCJ9LCJ4cy5zeXN0ZW0uYXR0cmlidXRlcyI6eyJ4cy5yb2xlY29sbGVjdGlvbnMiOlsiTGlicmFyeS1Vc2VyIl19LCJnaXZlbl9uYW1lIjoiWW9yZGFuIiwieHMudXNlci5hdHRyaWJ1dGVzIjp7fSwiZmFtaWx5X25hbWUiOiJCZXJvdiIsInN1YiI6ImMyMTRkNDI2LTA5MzUtNDhiNi05NTQ3LWM0NDQ5YzMzYzNmOSIsInNjb3BlIjpbIm9wZW5pZCIsIkxpYnJhcnktc2VjIXQzNDA4OS5Vc2VyIl0sImNsaWVudF9pZCI6InNiLUxpYnJhcnktc2VjIXQzNDA4OSIsImNpZCI6InNiLUxpYnJhcnktc2VjIXQzNDA4OSIsImF6cCI6InNiLUxpYnJhcnktc2VjIXQzNDA4OSIsImdyYW50X3R5cGUiOiJwYXNzd29yZCIsInVzZXJfaWQiOiJjMjE0ZDQyNi0wOTM1LTQ4YjYtOTU0Ny1jNDQ0OWMzM2MzZjkiLCJvcmlnaW4iOiJzYXAuZGVmYXVsdCIsInVzZXJfbmFtZSI6InlvcmRhbi5iZXJvdkBzYXAuY29tIiwiZW1haWwiOiJ5b3JkYW4uYmVyb3ZAc2FwLmNvbSIsImF1dGhfdGltZSI6MTYzMjIwOTIzMSwicmV2X3NpZyI6IjZmYTEzYzMwIiwiaWF0IjoxNjMyMjA5MjMxLCJleHAiOjE2MzIyNTI0MzEsImlzcyI6Imh0dHBzOi8vbGlicmFyeS1tYW5hZ21lbnQuYXV0aGVudGljYXRpb24uc2FwLmhhbmEub25kZW1hbmQuY29tL29hdXRoL3Rva2VuIiwiemlkIjoiYzgwZGY0YzEtN2I5NS00NTEzLWI4OGQtYTM3M2FjZTNiMjFkIiwiYXVkIjpbIkxpYnJhcnktc2VjIXQzNDA4OSIsInNiLUxpYnJhcnktc2VjIXQzNDA4OSIsIm9wZW5pZCJdfQ.1JwwDVUsbPCH8hfQNozUT5EiPGSCYKqwAjFByAr9mm2JLu2SRiLC9cOm4LfhCIdB-KpfxgQDRFf5DkdqbrNHzlvCcSIY02eGoMjMJeFt75Ybwi4rA2qKpaPsFrHKlA2L73HukJHZ_ws2bozJFUxZTvbSEm8YtDvK_bU_V8a1ga-WSnCJDl5cKTUVhSpS6TexmwVImZrJ9w-J2jh5DVKotYbwz7P67X4uC5EVKQkkCZ-Mwp5vzNr40RIzOGQw8PlpaGQHX9OSQmIIeyXd3nTNGkgwM-NCgYBY95n026X42qiSmSIlUqIjyVJr0ZiD3kGdjZ7smk_VFrjdluKeuQO0ZA";
    private MockMvc mockMvc;
    private static final String urlBase = "/api/v1/books";
    private static String userURL;
    
    @Autowired
    public EndpointTest(BookRepository bookRepository, UserRepository userRepository, BookRecordsRepository bookRecordsRepository, WebApplicationContext webApplicationContext) {
        this.bookRepository = bookRepository;
        this.userRepository = userRepository;
        this.bookRecordsRepository = bookRecordsRepository;
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
