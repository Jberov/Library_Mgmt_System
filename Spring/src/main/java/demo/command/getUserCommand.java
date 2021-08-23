package demo.command;

import demo.dto.UserDTO;
import demo.entities.Users;
import org.hibernate.QueryTimeoutException;
import org.hibernate.exception.DataException;
import org.hibernate.exception.JDBCConnectionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.InputMismatchException;

@RequestMapping("/api/v1/users")
@RestController
public class getUserCommand {
    @Autowired
    private UserDTO userDTO;

    @GetMapping(value = "/info/{name}")
    public Users getUser(@PathVariable("name") String name){
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
    @ExceptionHandler(MissingServletRequestParameterException.class)
    public String handleMissingParams(MissingServletRequestParameterException ex) {
        return ex.getParameterName() + " parameter is missing";
    }
    @ExceptionHandler(ResponseStatusException.class)
    public String handleWeb(ResponseStatusException responseStatusException){
        return responseStatusException.getLocalizedMessage();
    }

}
