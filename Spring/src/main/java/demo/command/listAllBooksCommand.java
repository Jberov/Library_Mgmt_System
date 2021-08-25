package demo.command;

import demo.dto.BookDTO;
import demo.entities.Books;
import org.hibernate.QueryTimeoutException;
import org.hibernate.exception.DataException;
import org.hibernate.exception.JDBCConnectionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import java.util.InputMismatchException;
import java.util.LinkedList;


@RestController
public class listAllBooksCommand {
    @Autowired
    private BookDTO bookDTO;

    @GetMapping(value = "api/v1/books")
    public ResponseEntity<?> execute() {
        try{
            if(bookDTO.getAllBooks().isEmpty()){
                return ResponseEntity.status(HttpStatus.NO_CONTENT).body("No books found");
            }else{
                return ResponseEntity.status(HttpStatus.FOUND).body(bookDTO.getAllBooks());
            }
        }catch (JDBCConnectionException jdbc) {
            return ResponseEntity.status(HttpStatus.GATEWAY_TIMEOUT).body("Error connecting to database");
        } catch (InputMismatchException ime) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid input");
        } catch (DataException dataException) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Data error");
        } catch (QueryTimeoutException qte) {
            return ResponseEntity.status(HttpStatus.GATEWAY_TIMEOUT).body("Database connection error");
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

