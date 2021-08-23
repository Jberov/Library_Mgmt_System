package demo.command;

import demo.dto.BookDTO;
import demo.entities.Books;
import org.hibernate.QueryTimeoutException;
import org.hibernate.exception.DataException;
import org.hibernate.exception.JDBCConnectionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
    public LinkedList<Books> execute() {
        try{
            if(bookDTO.getAllBooks().isEmpty()){
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, " No books ");
            }else{
                return bookDTO.getAllBooks();
            }
        }catch (JDBCConnectionException jdbc){
            System.out.println(jdbc.getMessage());
            throw new ResponseStatusException(HttpStatus.GATEWAY_TIMEOUT, "Error connecting to database");
        }catch (InputMismatchException ime){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid input");
        }catch(DataException dataException){
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Data error");
        }catch(QueryTimeoutException qte){
            System.out.println(qte.getMessage());
            throw new ResponseStatusException(HttpStatus.GATEWAY_TIMEOUT, "Database connection error");
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

