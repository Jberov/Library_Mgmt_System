package Library.demo.command;

import Library.demo.dao.BooksDAOImpl;
import Library.demo.entities.Books;
import com.nimbusds.oauth2.sdk.http.HTTPResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.LinkedList;


@RestController
public class List_all_books_admin_command {
    @Autowired
    BooksDAOImpl bookDAO;
    @GetMapping("/books/all")
    public LinkedList<Books> execute() {
            return bookDAO.get_all_books();

    }


}

