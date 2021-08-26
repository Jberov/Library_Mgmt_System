package tests.unitTests;

import demo.dao.BookRecordsDAO;
import demo.dao.UserDAOImpl;
import demo.dto.UserDTO;
import demo.dao.BooksDAOImpl;
import demo.entities.Users;
import demo.entities.Books;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.HashMap;
import java.util.LinkedList;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@WebMvcTest(UserDTO.class)
public class UsersDTOTests {
    @Autowired
    private MockMvc mvc;


    @MockBean
    private UserDTO userDTO;

    @MockBean
    private BooksDAOImpl booksDAO;

    @MockBean
    private UserDAOImpl userDAO;

    @MockBean
    private BookRecordsDAO bookRecordsDAO;


    @Test

    public void getUserTest() throws Exception{
        Users user = new Users("JBaller");
        BDDMockito.given(userDTO.getUser("JBaller")).willReturn(user);
        mvc.perform(MockMvcRequestBuilders.get("/api/v1/users/info/JBaller").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());
        //.andExpect(jsonPath("$", hasSize(is(2))))
        //.andExpect(jsonPath("$[0].name", is(user.getName())));

    }
    @Test

    public void leaseBook() throws Exception{
        Books books = new Books("978-17-82065-33-9",3,"Author","Name","Desc",true);
        Users user = new Users("JBaller");
        BDDMockito.given(userDTO.leaseBook(books.getIsbn(), user.getName())).willReturn(ResponseEntity.ok("Success"));
        mvc.perform(MockMvcRequestBuilders.patch("/api/v1/books/rental/978-17-82065-33-9&JBaller").contentType(MediaType.TEXT_PLAIN_VALUE))
                .andExpect(status().is4xxClientError());
                //.andExpect(content().string("Book successfully leased"));


    }
    @Test

    public void returnBook() throws Exception{
        Books books = new Books("978-17-82065-33-9",3,"Author","Name","Desc",true);
        Users user = new Users("JBaller");
        BDDMockito.given(userDTO.returnBook(books.getIsbn(), user.getName())).willReturn(ResponseEntity.ok("Success"));
        mvc.perform(MockMvcRequestBuilders.patch("/api/v1/books/return/978-06-79866-68-8&JBaller").contentType(MediaType.TEXT_PLAIN_VALUE))
                .andExpect(status().is4xxClientError());
                //.andExpect(content().string("Book successfully returned"));


    }
    @Test

    public void userUsedBooksTest() throws Exception{
        LinkedList<Books> takenList  = new LinkedList<>();
        LinkedList<Books> returnedList  = new LinkedList<>();
        HashMap<String,LinkedList<Books>> booksList = new HashMap<>();
        booksList.put("Currently taken books by user:", takenList);
        booksList.put("Already returned books by user:", returnedList);
        Books books = new Books("978-17-82065-33-9",3,"Author","Name","Desc",true);
        Users user = new Users("JBaller");
        takenList.add(books);
        BDDMockito.given(userDTO.userUsedBooks( user.getName())).willReturn(booksList);
        mvc.perform(MockMvcRequestBuilders.get("/api/v1/users/history/JBaller").contentType(MediaType.TEXT_PLAIN_VALUE))
                .andExpect(status().is4xxClientError());
    }

    @Test

    public void getUsersByBookTest() throws Exception{
        LinkedList<String> userList  = new LinkedList<>();
        Books books = new Books("978-17-82065-33-9",3,"Author","Name","Desc",true);
        Users user = new Users("JBaller");
        userList.add(user.getName());
        BDDMockito.given(userDTO.getUsersByBook(books.getIsbn())).willReturn(userList);
        mvc.perform(MockMvcRequestBuilders.get("/api/v1/users/byBook/978-17-82065-33-9").contentType(MediaType.TEXT_PLAIN_VALUE))
                .andExpect(status().is4xxClientError());
    }
}
