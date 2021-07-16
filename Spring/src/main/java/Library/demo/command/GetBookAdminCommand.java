package Library.demo.command;

import Library.demo.dao.BooksDAOImpl;
import Library.demo.entities.Books;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GetBookAdminCommand {
    @Autowired
    BooksDAOImpl bookDAO;

    @GetMapping("books/book")
    public Books getBook(@RequestParam long isbn){
        return bookDAO.getBook(isbn);
    }

}
