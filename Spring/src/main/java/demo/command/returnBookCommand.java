package demo.command;

import demo.dto.UserDTO;
import net.minidev.json.JSONObject;
import org.hibernate.QueryTimeoutException;
import org.hibernate.exception.DataException;
import org.hibernate.exception.JDBCConnectionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.InputMismatchException;
import java.util.NoSuchElementException;


@RestController
public class returnBookCommand {
    @Autowired
    private UserDTO userDTO;

    @PatchMapping(value = "api/v1/books/return/{isbn}&{username}")
    public ResponseEntity<JSONObject> returnBook(@PathVariable("isbn") @Valid String isbn, @PathVariable("username") String username){
        JSONObject result = new JSONObject();
        try{
            result.put("response",userDTO.returnBook(isbn, username));
            return ResponseEntity.status(HttpStatus.OK).body(result);
        } catch (JDBCConnectionException jdbc) {
            result.put("error","Error connecting to database");
            return ResponseEntity.status(HttpStatus.GATEWAY_TIMEOUT).body(result);
        } catch (InputMismatchException ime) {
            result.put("error","Invalid input");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);
        } catch (DataException dataException) {
            result.put("error","Data error");
            return ResponseEntity.status(HttpStatus.CONFLICT).body(result);
        } catch (QueryTimeoutException qte) {
            result.put("error","Database connection error");
            return ResponseEntity.status(HttpStatus.GATEWAY_TIMEOUT).body(result);
        }catch (NoSuchElementException nsee){
            result.put("error","No records for book with such isbn");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(result);
        }catch (Exception e){
            result.put("error","Unknown error");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(result);
        }
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseBody
    public ResponseEntity<String> validationError(MethodArgumentNotValidException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid isbn number");
    }
}
