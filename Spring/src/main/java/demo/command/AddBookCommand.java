package demo.command;

import demo.dto.BookDTO;
import demo.entities.Books;
import net.minidev.json.JSONObject;
import org.hibernate.QueryTimeoutException;
import org.hibernate.exception.DataException;
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
    private BookDTO bookDTO;

    @PostMapping(value = "/api/v1/books",consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<JSONObject> execute (@RequestBody @Valid  Books book) {

        JSONObject result = new JSONObject();
        try {
            result.put("response",bookDTO.addBookAdmin(book.getIsbn(), book.getCount(),book.getAuthor(),book.getName(),book.getDescription()));
            return ResponseEntity.ok().body(result);
        } catch (JDBCConnectionException jdbc) {
            result.put("error","Error connecting to database");
            return ResponseEntity.status(HttpStatus.GATEWAY_TIMEOUT).body(result);
        } catch (InputMismatchException ime) {
            result.put("error","Invalid input");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);
        } catch (Exception e){
            result.put("error","Error");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(result);
        }
    }

    @ExceptionHandler (MethodArgumentNotValidException.class)
    @ResponseStatus (HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ResponseEntity<String> validationError (MethodArgumentNotValidException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid isbn number");
    }
}