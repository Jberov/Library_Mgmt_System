package Library.demo;

import demo.LibraryApplication;
import demo.dao.BookRecordsDAO;
import demo.dao.BooksDAOImpl;
import demo.dao.UserDAOImpl;
import demo.dto.BookDTO;
import demo.entities.Books;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.LinkedList;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.Matchers.*;

@ContextConfiguration(classes = LibraryApplication.class)
@RunWith(SpringRunner.class)
@WebMvcTest(BookDTO.class)
public class BooksDAOUnitTests {
    @Autowired
    private MockMvc mvc;

    @Autowired
    private BookDTO bookDTO;

    @MockBean
    private BooksDAOImpl booksDAO;

    @MockBean
    private UserDAOImpl userDAO;

    @MockBean
    private BookRecordsDAO bookRecordsDAO;

    @Test
    public void getAllBooks() throws Exception {
        Books book = new Books(3, "Иван Вазов", "Под Игото", "В малко градче пристига странник и им показва значението на свободата",true);
        LinkedList<Books> books = new LinkedList<>();
        books.add(book);
        BDDMockito.given(booksDAO.getAllBooks()).willReturn(books);

        mvc.perform(MockMvcRequestBuilders.get("/admin/books/all")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].author", is(book.getAuthor())));
    }

    @Test
    public void addBook() throws Exception {
        mvc.perform(MockMvcRequestBuilders.post("/admin/addBook?count_books=2&author=Lord Vader&name=Dark Side&description=Desc")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());


    }

    @Test
    public void deleteBook() throws Exception {
        mvc.perform(MockMvcRequestBuilders.delete("/admin/books/delete?name=Под Игото")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void getBookById() throws Exception {


        mvc.perform(MockMvcRequestBuilders.get("/admin/getBook?isbn=0")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());

    }
}