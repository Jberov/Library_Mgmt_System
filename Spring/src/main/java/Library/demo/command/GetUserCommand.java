package Library.demo.command;

import Library.demo.dto.UserDTO;
import Library.demo.entities.Users;
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

@RestController
public class GetUserCommand {
    @Autowired
    private UserDTO userDTO;

    @GetMapping("/users/profile")
    public Users getUser(@RequestParam String name){
        try{
            if(userDTO.getUser(name) == null){
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No such user");
            }else{
                return userDTO.getUser(name);
            }
        }catch (JDBCConnectionException jdbc){
            throw new ResponseStatusException(HttpStatus.GATEWAY_TIMEOUT, "Error connecting to database");
        }catch (InputMismatchException ime){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid input");
        }catch(DataException dataException){
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Data error");
        }catch(QueryTimeoutException qte){
            throw new ResponseStatusException(HttpStatus.GATEWAY_TIMEOUT, "Database connection error");
        }catch (NullPointerException nullPointerException){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No records for this user");
        }


    }
}
