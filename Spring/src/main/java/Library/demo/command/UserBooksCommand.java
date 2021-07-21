package Library.demo.command;

import Library.demo.dto.UserDTO;
import Library.demo.entities.Books;
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
public class UserBooksCommand {
    @Autowired
    UserDTO userDTO;

    @GetMapping("user/history")
    public LinkedList<Books> userHistory(@RequestParam String username){
        try{
            if(userDTO.userUsedBooks(username)==null){
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, " No books ");
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
        }
    }
}
