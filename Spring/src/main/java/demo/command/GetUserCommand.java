package demo.command;

import demo.dto.UserDTO;
import net.minidev.json.JSONObject;
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
import java.util.NoSuchElementException;

@RequestMapping("/api/v1/users")
@RestController
public class GetUserCommand {
    @Autowired
    private UserDTO userDTO;

    @GetMapping(value = "/info/{name}")
    public ResponseEntity<JSONObject> getUser (@PathVariable("name") String name){
        JSONObject result = new JSONObject();
        try{
            if (userDTO.getUser(name) == null) {
                result.put("error","No such user");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(result);
            }
            result.put("user",userDTO.getUser(name));
            return ResponseEntity.status(HttpStatus.OK).body(result);

        } catch (JDBCConnectionException jdbc) {
            result.put("error","Error connecting to database");
            return ResponseEntity.status(HttpStatus.GATEWAY_TIMEOUT).body(result);
        } catch (InputMismatchException ime) {
            result.put("error","Invalid input");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);
        } catch (Exception e){
            result.put("error","Unknown error");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(result);
        }


    }
    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<String> handleMissingParams (MissingServletRequestParameterException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Missing parameter(s): " + ex.getParameterName());
    }
    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<String> handleWeb (ResponseStatusException responseStatusException) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseStatusException.getMessage());
    }


}
