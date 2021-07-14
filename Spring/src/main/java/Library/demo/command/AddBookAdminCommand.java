package Library.demo.command;

import Library.demo.dao.BooksDAOImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@CrossOrigin
@RestController
public class AddBookAdminCommand {
    @Autowired
    BooksDAOImpl bookDAO;

    @PostMapping("/books/add")
    public void execute(@RequestParam int count_books, @RequestParam String author, @RequestParam String name, @RequestParam String description) {
        bookDAO.addBookAdmin(count_books, author, name, description);
    }
}

