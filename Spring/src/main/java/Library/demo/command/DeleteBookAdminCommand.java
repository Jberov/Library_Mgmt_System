package Library.demo.command;

import Library.demo.dao.BooksDAOImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin
@RestController
public class DeleteBookAdminCommand {
    @Autowired
    BooksDAOImpl bookDAO;

    @DeleteMapping("/books/delete")
    public String execute(@RequestParam String name){
        return bookDAO.deleteBookAdmin(name);
    }
}
