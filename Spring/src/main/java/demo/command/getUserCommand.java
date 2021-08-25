package demo.command;

import demo.dto.UserDTO;
import demo.entities.Users;
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

import java.util.InputMismatchException;

@RequestMapping("/api/v1/users")
@RestController
public class getUserCommand {
    @Autowired
    private UserDTO userDTO;

    @GetMapping(value = "/info/{name}")
    public ResponseEntity<?> getUser(@PathVariable("name") String name){
        try{
            if(userDTO.getUser(name) == null){
                return ResponseEntity.status(HttpStatus.NO_CONTENT).body("No such user");
            }else{
                return ResponseEntity.status(HttpStatus.FOUND).body(userDTO.getUser(name));
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
    public String handleMissingParams(MissingServletRequestParameterException ex) {
        return ex.getParameterName() + " parameter is missing";
    }
    @ExceptionHandler(ResponseStatusException.class)
    public String handleWeb(ResponseStatusException responseStatusException){
        return responseStatusException.getLocalizedMessage();
    }


}
