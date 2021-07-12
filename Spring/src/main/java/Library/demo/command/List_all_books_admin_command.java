package Library.demo.command;

import Library.demo.dao.BooksDAOImpl;
import org.hibernate.NonUniqueObjectException;
import org.hibernate.exception.JDBCConnectionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.InputMismatchException;



@RestController
public class List_all_books_admin_command {
    @Autowired
    BooksDAOImpl bookDAO;

    @GetMapping("/books/add")
    public String execute(@RequestParam int count_books, @RequestParam String author, @RequestParam String name, @RequestParam String description) {
        try {
            bookDAO.addBook_admin(count_books, author, name, description);
            return "Book added successfully";
        }catch (InputMismatchException ime){
            return "Wrong format!";
        }catch (JDBCConnectionException jdbcConnectionException){
            return "Error connecting to the database";
        }catch (NonUniqueObjectException objectException){
            return "Such book already exists";
        }
    }
}

