package demo.command;

import demo.dto.UserDTO;
import demo.entities.Books;
import org.hibernate.QueryTimeoutException;
import org.hibernate.exception.DataException;
import org.hibernate.exception.JDBCConnectionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
    public LinkedList<LinkedList<Books>> userHistory(@PathVariable("username") String username) throws ResponseStatusException{
        try{
            if(userDTO.userUsedBooks(username)==null){
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No books or no such user");
            }else{
                return userDTO.userUsedBooks(username);
            }
        }catch (JDBCConnectionException jdbc){
            throw new ResponseStatusException(HttpStatus.GATEWAY_TIMEOUT, "Error connecting to database");
        }catch (InputMismatchException ime){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid input");
        }catch(DataException dataException){
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Data error");
        }catch(QueryTimeoutException qte){
            throw new ResponseStatusException(HttpStatus.GATEWAY_TIMEOUT, "Database connection error");
        }catch (NullPointerException npte){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, " No such user ");
        }
    }
    @ExceptionHandler(MissingServletRequestParameterException.class)
    public String handleMissingParams(MissingServletRequestParameterException ex) {
        return ex.getParameterName() + " parameter is missing";
    }

}
