package Library.demo.command;

import Library.demo.dto.BookDTO;
import org.hibernate.QueryTimeoutException;
import org.hibernate.exception.DataException;
import org.hibernate.exception.JDBCConnectionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.InputMismatchException;

@CrossOrigin
@RestController
public class AddBookAdminCommand {
    @Autowired
    private BookDTO bookDTO;

    @PostMapping("/books/add")
    public String execute(@RequestParam int count_books, @RequestParam String author, @RequestParam String name, @RequestParam String description) {
        try {
            if (bookDTO.addBookAdmin(count_books, author, name, description)) {
                return "Success";
            } else {
                return "Failure. Book not added to database";
            }
        } catch (JDBCConnectionException jdbc) {
            throw new ResponseStatusException(HttpStatus.GATEWAY_TIMEOUT, "Error connecting to database");
        } catch (InputMismatchException ime) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid input");
        } catch (DataException dataException) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Data error");
        } catch (QueryTimeoutException qte) {
            throw new ResponseStatusException(HttpStatus.GATEWAY_TIMEOUT, "Database connection error");
        }
    }
}

