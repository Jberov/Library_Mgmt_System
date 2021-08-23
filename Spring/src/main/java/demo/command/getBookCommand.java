package demo.command;

import demo.dto.BookDTO;
import demo.entities.Books;
import org.hibernate.QueryTimeoutException;
import org.hibernate.exception.DataException;
import org.hibernate.exception.JDBCConnectionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
    public Books getBook(@Valid @PathVariable("isbn") String isbn){
        System.out.println(isbn);
        try{
            if(bookDTO.getBookById(isbn) != null){
                return bookDTO.getBookById(isbn);
            }else{
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, " No such book");
            }
        }catch (JDBCConnectionException jdbc){
            throw new ResponseStatusException(HttpStatus.GATEWAY_TIMEOUT, "Error connecting to database");
        }catch (InputMismatchException ime){
            System.out.println(ime.getLocalizedMessage());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid input");
        }catch(DataException dataException){
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Data error");
        }catch(QueryTimeoutException qte){
            throw new ResponseStatusException(HttpStatus.GATEWAY_TIMEOUT, "Database connection error");
        }catch (NullPointerException npte){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, " No such book");
        }
    }

    @ExceptionHandler(ResponseStatusException.class)
    public String handleWeb(ResponseStatusException responseStatusException){
        return responseStatusException.getLocalizedMessage();
    }
}
