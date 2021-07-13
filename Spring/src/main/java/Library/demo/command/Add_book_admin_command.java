package Library.demo.command;

import Library.demo.dao.BooksDAOImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
public class Add_book_admin_command {
    @Autowired
    BooksDAOImpl bookDAO;

    @PostMapping("/books/add")
    public void execute(@RequestParam int count_books, @RequestParam String author, @RequestParam String name, @RequestParam String description) {
            bookDAO.addBook_admin(count_books, author, name, description);

    }
}

