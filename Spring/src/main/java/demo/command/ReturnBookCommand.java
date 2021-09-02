package demo.command;

import demo.services.UserService;
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
public class ReturnBookCommand {
    @Autowired
    private UserService userService;

    @PatchMapping(value = "api/v1/books/return/{isbn}&{username}")
    public ResponseEntity<JSONObject> returnBook (@PathVariable("isbn") @Valid String isbn, @PathVariable("username") String username) {
        JSONObject result = new JSONObject();
        try{
            switch (userService.returnBook(isbn, username)){
                case ("No such user"):
                case ("You have not taken this book!"):
                case ("No such book exists"):
                    result.put("response", userService.returnBook(isbn, username));
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(result);
                case ("Book successfully returned"):
                    result.put("response", userService.returnBook(isbn, username));
                    return ResponseEntity.status(HttpStatus.OK).body(result);
            }
            return ResponseEntity.status(HttpStatus.OK).body(result);
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

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseBody
    public ResponseEntity<String> validationError (MethodArgumentNotValidException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid isbn number");
    }
}
