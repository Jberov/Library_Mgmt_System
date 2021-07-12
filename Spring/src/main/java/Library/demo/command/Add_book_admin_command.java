package Library.demo.command;

import Library.demo.dao.BooksDAOImpl;
import Library.demo.entities.Books;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.LinkedList;

public class Add_book_admin_command {
    @Autowired
    BooksDAOImpl bookDAO;

    @GetMapping("/books/all")
    public LinkedList<Books> execute() {
        return bookDAO.get_all_books();
    }
}

