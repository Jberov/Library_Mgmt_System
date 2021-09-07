package demo.command;

import com.sap.cloud.security.xsuaa.token.SpringSecurityContext;
import demo.services.BookService;
import net.minidev.json.JSONObject;
import org.hibernate.exception.JDBCConnectionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;


@RestController
public class ListAllBooksCommand {
    @Autowired
    private BookService bookService;

    @GetMapping(value = "api/v1/books")
    public ResponseEntity<JSONObject> execute() {
        JSONObject result = new JSONObject();
        try{
            if (bookService.getAllBooks().isEmpty()) {
                result.put("error","No books found");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(result);
            }
            result.put("books", bookService.getAllBooks());
            return ResponseEntity.status(HttpStatus.OK).body(result);

        } catch (JDBCConnectionException jdbc) {
            result.put("error","Error connecting to database");
            return ResponseEntity.status(HttpStatus.GATEWAY_TIMEOUT).body(result);
        } catch (Exception e){
            System.out.println(e.getMessage());
            result.put("error","Error, service is currently unavailable");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(result);
        }
    }
    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<String> handleMissingParams (MissingServletRequestParameterException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Missing parameter(s): " + ex.getParameterName());
    }
    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<String> handleWeb (ResponseStatusException responseStatusException) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseStatusException.getMessage());
    }
}

