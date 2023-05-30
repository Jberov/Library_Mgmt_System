package unitTest;

import demo.LibraryApplication;
import demo.dao.BookRecordsDAO;
import demo.dao.BooksDAOImpl;
import demo.dao.UserDAOImpl;
import demo.dto.BookDTO;
import demo.mappers.BookMapper;
import demo.mappers.UserMapper;
import demo.services.UserService;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ActiveProfiles("test")
@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = {
        LibraryApplication.class})
@ContextConfiguration(classes = LibraryApplication.class)
public class UserServiceTest {
    private static final List<String> list = new ArrayList<>();
    private static final Set<String> set = new HashSet<>();
    private static final String username = "";
    private static final BookDTO bookDTO = new BookDTO("978-0-09-959008-8",7,"Ювал Харари","Кратка история на Хомо Сапиенс","История", "Estetika");
    private static final Map<String, List<String>> history = new HashMap<String, List<String>>() {
    };
   
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

    @Test
    public void getUserTest() {
        //UserDTO userDTO = new UserDTO(username);
       // BDDMockito.given(userService.getUser(username)).willReturn(userDTO);
        //Assertions.assertEquals(username, userDTO.getEmail());
    }
    
    @Test
    public void getNullUserTest() {
        BDDMockito.given(userService.getUser(username + "7")).willReturn(null);
        Assertions.assertNull(userService.getUser(username + "7"));
    }

    @Test
    public void leaseBook(){
        BDDMockito.given(userService.leaseBook(bookDTO.getIsbn(),username)).willReturn(bookDTO);
        Assertions.assertEquals(bookDTO.getIsbn(), "978-0-09-959008-8");
    }

    @Test
    public void returnBook(){
        bookDTO.setCount(8);
        BDDMockito.given(userService.returnBook(bookDTO.getIsbn(),username)).willReturn(bookDTO);
        Assertions.assertEquals(bookDTO.getCount(), 8);
    }

    @Test
    public void userUsedBooksTestEmptyHistory() {
        list.clear();
        list.add("Кратка история на Хомо Сапиенс, Ювал Харари");
        history.put("Already returned books by user:",list);
        BDDMockito.given(userService.userUsedBooks(username)).willReturn(history);
        Assertions.assertEquals(userService.userUsedBooks(username),history);
    }

    @Test
    public void getUsersByBookTest() {
        BDDMockito.given(userService.getUsersByBook(bookDTO.getIsbn())).willReturn(set);
        Assertions.assertEquals(userService.getUsersByBook(bookDTO.getIsbn()), set);
    }
    
    @Test
    public void getUsersByNullBookTest() {
        BDDMockito.given(userService.getUsersByBook("978-0-09-959808-9")).willReturn(set);
        Assertions.assertTrue(list.isEmpty());
    }
}
