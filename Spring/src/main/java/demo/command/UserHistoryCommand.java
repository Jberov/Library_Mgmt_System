package demo.command;

import demo.services.UserService;
import net.minidev.json.JSONObject;
import org.hibernate.exception.JDBCConnectionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.*;
import java.util.InputMismatchException;

@RestController
public class UserHistoryCommand {

    @Autowired
    UserService userService;

    @GetMapping(value = "api/v1/users/history")
    public ResponseEntity<JSONObject> userHistory () {
        JSONObject result = new JSONObject();
        try {
            if (userService.userUsedBooks("JBaller") == null) {
                result.put("error", "No books or no such user");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(result);
            }
            result.put(SecurityContextHolder.getContext().getAuthentication().getName().intern() + "'s history", userService.userUsedBooks("JBaller"));
            return ResponseEntity.status(HttpStatus.OK).body(result);

        } catch (JDBCConnectionException jdbc) {
            result.put("error","Error connecting to database");
            return ResponseEntity.status(HttpStatus.GATEWAY_TIMEOUT).body(result);
        } catch (InputMismatchException ime) {
            result.put("error","Invalid input");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            result.put("error","Error, service is currently unavailable");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(result);
        }
    }
    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<String> handleMissingParams (MissingServletRequestParameterException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Missing parameter(s): " + ex.getParameterName());
    }

}
