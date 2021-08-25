package demo.command;

import demo.dto.BookDTO;
import demo.entities.Books;
import org.hibernate.QueryTimeoutException;
import org.hibernate.exception.DataException;
import org.hibernate.exception.JDBCConnectionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import javax.validation.Valid;
import java.util.InputMismatchException;

@RequestMapping("/api/v1")
@RestController
public class getBookCommand {
    @Autowired
    private BookDTO bookDTO;

    @GetMapping(value = "/books/{isbn}")
    public ResponseEntity<?> getBook( @PathVariable("isbn") @Valid String isbn) throws ResponseStatusException{
        try{
            if(bookDTO.getBookById(isbn) != null){
                return ResponseEntity.status(HttpStatus.FOUND).body(bookDTO.getBookById(isbn));
            }else{
                return ResponseEntity.status(HttpStatus.NO_CONTENT).body("No such book");
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
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body("No such book");
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Error");
        }
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ResponseEntity<String> validationError(MethodArgumentNotValidException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid isbn number");
    }
}

