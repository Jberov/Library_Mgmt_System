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
import java.util.InputMismatchException;

@RestController
public class userHistoryCommand {
    @Autowired
    UserDTO userDTO;

    @GetMapping(value = "api/v1/users/history/{username}")
    public ResponseEntity<JSONObject> userHistory(@PathVariable("username") String username){
        JSONObject result = new JSONObject();
        try{
            if(userDTO.userUsedBooks(username)==null){
                result.put("error", "No books or no such user");
                return ResponseEntity.status(HttpStatus.NO_CONTENT).body(result);
            }else{
                result.put(username + "'s history", userDTO.userUsedBooks(username));
                return ResponseEntity.status(HttpStatus.OK).body(result);
            }
        }catch (JDBCConnectionException jdbc) {
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
        }catch(NullPointerException npe){
            result.put("error","No such user");
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(result);
        }catch (Exception e){
            result.put("error","Error");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(result);
        }
    }
    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<String> handleMissingParams(MissingServletRequestParameterException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Missing parameter(s): " + ex.getParameterName());
    }

}
