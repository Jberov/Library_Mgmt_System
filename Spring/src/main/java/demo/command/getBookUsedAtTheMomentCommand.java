package demo.command;

import demo.dto.UserDTO;
import net.minidev.json.JSONObject;
import org.hibernate.QueryTimeoutException;
import org.hibernate.exception.DataException;
import org.hibernate.exception.JDBCConnectionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.InputMismatchException;


@RestController
public class getBookUsedAtTheMomentCommand {
    @Autowired
    private UserDTO userDTO;
    @GetMapping(value = "/api/v1/users/byBook/{isbn}")
    public ResponseEntity<JSONObject> getUsersOfBook(@PathVariable ("isbn") @Valid String isbn){
        JSONObject result = new JSONObject();
        try{
            if((userDTO.getUsersByBook(isbn) !=null) && (!userDTO.getUsersByBook(isbn).isEmpty())){
                result.put("book",userDTO.getUsersByBook(isbn));
                return ResponseEntity.status(HttpStatus.OK).body(result);
            }else if(userDTO.getUsersByBook(isbn).isEmpty()){
                result.put("error","No users have taken this book");
                return ResponseEntity.status(HttpStatus.NO_CONTENT).body(result);
            }else{
                result.put("error","No such book");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(result);
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
            result.put("error","No such book");
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(result);
        }catch (Exception e){
            result.put("error","Error");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(result);
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
