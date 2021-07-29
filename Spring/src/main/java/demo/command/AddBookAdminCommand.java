package demo.command;

import demo.dto.BookDTO;
import org.hibernate.QueryTimeoutException;
import org.hibernate.exception.DataException;
import org.hibernate.exception.JDBCConnectionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.InputMismatchException;

@CrossOrigin
@RestController
public class AddBookAdminCommand {
    @Autowired
    private BookDTO bookDTO;

    @PostMapping("/admin/addBook")
    public String execute(@RequestParam int count_books, @RequestParam String author, @RequestParam String name, @RequestParam String description) {
        try {
            return bookDTO.addBookAdmin(count_books, author, name, description);
        } catch (JDBCConnectionException jdbc) {
            return "Error connecting to database";
        } catch (InputMismatchException ime) {
           return  "Invalid input";
        } catch (DataException dataException) {
           return "Data error";
        } catch (QueryTimeoutException qte) {
            return "Database connection error";
        }catch (Exception e){
            System.out.println(e.getMessage());
            return "Error";
        }
    }
}

