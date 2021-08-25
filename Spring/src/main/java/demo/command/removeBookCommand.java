package demo.command;


import demo.dto.BookDTO;
import org.hibernate.QueryTimeoutException;
import org.hibernate.exception.DataException;
import org.hibernate.exception.JDBCConnectionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.InputMismatchException;
import java.util.NoSuchElementException;


@RestController
public class removeBookCommand {
    @Autowired
    BookDTO bookDTO;

    @DeleteMapping(value = "api/v1/books/{isbn}")
    public ResponseEntity<String> execute(@Valid @PathVariable("isbn") String isbn){
        try{
            return ResponseEntity.status(HttpStatus.OK).body(bookDTO.deleteBook(isbn));
        } catch (JDBCConnectionException jdbc) {
            return ResponseEntity.status(HttpStatus.GATEWAY_TIMEOUT).body("Error connecting to database");
        } catch (InputMismatchException ime) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid input");
        } catch (DataException dataException) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Data error");
        } catch (QueryTimeoutException qte) {
            return ResponseEntity.status(HttpStatus.GATEWAY_TIMEOUT).body("Database connection error");
        }catch (NoSuchElementException nsee){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body( "No records for book with such isbn");
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
