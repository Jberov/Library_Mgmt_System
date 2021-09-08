package demo.command;

import demo.dto.BookDTO;
import demo.services.BookService;
import net.minidev.json.JSONObject;
import org.hibernate.exception.JDBCConnectionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.InputMismatchException;

@RestController
public class AddBookCommand {
    @Autowired
    private BookService bookService;

    @PostMapping(value = "/api/v1/books",consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<JSONObject> execute (@RequestBody @Valid BookDTO book) {

        JSONObject result = new JSONObject();
        try {
            BookDTO addedBook = bookService.addBook(book);
            if(addedBook == null){
                result.put("response","You have entered the isbn of an existing book, yet the name does not match. Request denied");
                return ResponseEntity.badRequest().body(result);
            }
            result.put("message","Success. Here is the newly added book");
            result.put("response",addedBook);
            return ResponseEntity.status(HttpStatus.CREATED).body(result);
        } catch (JDBCConnectionException jdbc) {
            result.put("error","Error connecting to database");
            return ResponseEntity.status(HttpStatus.GATEWAY_TIMEOUT).body(result);
        } catch (InputMismatchException ime) {
            result.put("error","Invalid input");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);
        } catch (Exception e){
            System.out.println(e.getMessage());
            result.put("error","Error, service is currently unavailable");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(result);
        }
    }

    @ExceptionHandler (MethodArgumentNotValidException.class)
    @ResponseStatus (HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ResponseEntity<String> validationError (MethodArgumentNotValidException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid isbn number");
    }
}