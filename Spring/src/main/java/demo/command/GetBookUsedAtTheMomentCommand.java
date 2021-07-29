package demo.command;

import demo.dto.UserDTO;
import org.hibernate.QueryTimeoutException;
import org.hibernate.exception.DataException;
import org.hibernate.exception.JDBCConnectionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.InputMismatchException;
import java.util.LinkedList;

@RestController
public class GetBookUsedAtTheMomentCommand {
    @Autowired
    private UserDTO userDTO;
    @GetMapping("/admin/book/users")
    public LinkedList<String> getUsersOfBook(@RequestParam long isbn){
        try{
            if((userDTO.getUsersByBook(isbn) !=null) && (!userDTO.getUsersByBook(isbn).isEmpty())){
                return userDTO.getUsersByBook(isbn);
            }else if(userDTO.getUsersByBook(isbn).isEmpty()){
                throw new ResponseStatusException(HttpStatus.NO_CONTENT, "No users have taken this book");
            }else{
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No such book");
            }
        }catch (JDBCConnectionException jdbc){
            throw new ResponseStatusException(HttpStatus.GATEWAY_TIMEOUT, "Error connecting to database");
        }catch (InputMismatchException ime){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid input");
        }catch(DataException dataException){
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Data error");
        }catch(QueryTimeoutException qte){
            throw new ResponseStatusException(HttpStatus.GATEWAY_TIMEOUT, "Database connection error");
        }

    }
}
