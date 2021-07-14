package Library.demo.command;

import Library.demo.dao.BooksDAOImpl;
import Library.demo.entities.Books;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.LinkedList;


@RestController
public class ListAllBooksAdminCommand {
    @Autowired
    BooksDAOImpl bookDAO;
    @GetMapping("/books/all")
    public LinkedList<Books> execute() {
            return bookDAO.getAllBooks();

    }


}

