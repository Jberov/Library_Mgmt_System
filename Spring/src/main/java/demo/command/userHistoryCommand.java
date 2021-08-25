package demo.command;

import demo.dto.UserDTO;
import demo.entities.Books;
import org.hibernate.QueryTimeoutException;
import org.hibernate.exception.DataException;
import org.hibernate.exception.JDBCConnectionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import java.util.InputMismatchException;
import java.util.LinkedList;

@RestController
public class userHistoryCommand {
    @Autowired
    UserDTO userDTO;

    @GetMapping(value = "api/v1/users/history/{username}")
    public ResponseEntity<?> userHistory(@PathVariable("username") String username) throws ResponseStatusException{
        try{
            if(userDTO.userUsedBooks(username)==null){
                return ResponseEntity.status(HttpStatus.NO_CONTENT).body("No books or no such user");
            }else{
                return ResponseEntity.status(HttpStatus.NO_CONTENT).body(userDTO.userUsedBooks(username));
            }
        }catch (JDBCConnectionException jdbc) {
            return ResponseEntity.status(HttpStatus.GATEWAY_TIMEOUT).body("Error connecting to database");
        } catch (InputMismatchException ime) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid input");
        } catch (DataException dataException) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Data error");
        } catch (QueryTimeoutException qte) {
            return ResponseEntity.status(HttpStatus.GATEWAY_TIMEOUT).body("Database connection error");
        }catch(NullPointerException npe){
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body("No such user");
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Error");
        }
    }
    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<String> handleMissingParams(MissingServletRequestParameterException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Missing parameter(s): " + ex.getParameterName());
    }

}
