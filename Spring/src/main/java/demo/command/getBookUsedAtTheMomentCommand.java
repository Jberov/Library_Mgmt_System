package demo.command;

import demo.dto.UserDTO;
import org.hibernate.QueryTimeoutException;
import org.hibernate.exception.DataException;
import org.hibernate.exception.JDBCConnectionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.util.InputMismatchException;
import java.util.LinkedList;


@RestController
public class getBookUsedAtTheMomentCommand {
    @Autowired
    private UserDTO userDTO;
    @GetMapping(value = "/api/v1/users/byBook/{isbn}")
    public ResponseEntity<?> getUsersOfBook(@PathVariable ("isbn") @Valid String isbn){
        try{
            if((userDTO.getUsersByBook(isbn) !=null) && (!userDTO.getUsersByBook(isbn).isEmpty())){
                return ResponseEntity.status(HttpStatus.OK).body(userDTO.getUsersByBook(isbn));
            }else if(userDTO.getUsersByBook(isbn).isEmpty()){
                return ResponseEntity.status(HttpStatus.NO_CONTENT).body( "No users have taken this book");
            }else{
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No such book");
            }
        }catch (JDBCConnectionException jdbc){
            return ResponseEntity.status(HttpStatus.GATEWAY_TIMEOUT).body("Error connecting to database");
        }catch (InputMismatchException ime){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid input");
        }catch(DataException dataException){
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Data error");
        }catch(QueryTimeoutException qte){
            return ResponseEntity.status(HttpStatus.GATEWAY_TIMEOUT).body("Database connection error");
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Error");
        }
    }
    @ExceptionHandler(MissingServletRequestParameterException.class)
    public String handleMissingParams(MissingServletRequestParameterException ex) {
        return ex.getParameterName() + " parameter is missing";
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ResponseEntity<String> validationError(MethodArgumentNotValidException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid isbn number");
    }

}
