package Library.demo;

import Library.demo.dao.BooksDAOImpl;
import Library.demo.entities.Books;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.LinkedList;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.Matchers.*;


@RunWith(SpringRunner.class)
@WebMvcTest(BooksDAOImpl.class)
public class BooksDAOUnitTests {
    @Autowired
    private MockMvc mvc;

    @MockBean
    private BooksDAOImpl booksDAO;

    @Test
    public void getAllBooks() throws Exception {
        Books book = new Books(3,"Иван Вазов", "Под Игото", "В малко градче пристига странник и им показва значението на свободата");
        LinkedList<Books> books = new LinkedList<>();
        books.add(book);
        BDDMockito.given(booksDAO.getAllBooks()).willReturn(books);

        mvc.perform(MockMvcRequestBuilders.get("/books/all")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].author", is(book.getAuthor())));
    }

    @Test
    public void addBook() throws Exception { ;
        mvc.perform(MockMvcRequestBuilders.post("/books/add?count_books=2&author=Lord Vader&name=Dark Side&description=Desc")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());


    }

    @Test
    public void deleteBook() throws Exception{
        mvc.perform(MockMvcRequestBuilders.delete("/books/delete?name=Под Игото")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
}
