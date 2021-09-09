package demo.command;


import demo.dto.BookDTO;
import demo.services.BookService;
import net.minidev.json.JSONObject;
import org.hibernate.exception.JDBCConnectionException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.InputMismatchException;


@RestController
public class RemoveBookCommand {
    @Autowired
    BookService bookService;

    @DeleteMapping(value = "api/v1/books/{isbn}")
    public ResponseEntity<JSONObject> execute(@Valid @PathVariable("isbn") String isbn){
    JSONObject result = new JSONObject();
        try{
            BookDTO deletedBook = bookService.deleteBook(isbn);
            if(deletedBook == null) {
                result.put("error", "No such book exists or not all users have returned it yet.");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(result);
            }
            result.put("Message", "Book successfully deleted");
            result.put("response", deletedBook);
            return ResponseEntity.status(HttpStatus.OK).body(result);
        } catch (JDBCConnectionException jdbc) {
            result.put("error","Error connecting to database");
            return ResponseEntity.status(HttpStatus.GATEWAY_TIMEOUT).body(result);
        } catch (InputMismatchException ime) {
            result.put("error","Invalid input");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            result.put("error","Error, service is currently unavailable");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(result);
        }
    }


    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ResponseEntity<String> validationError(MethodArgumentNotValidException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid isbn number");
    }
}
