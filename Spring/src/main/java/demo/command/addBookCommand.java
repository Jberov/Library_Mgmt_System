package demo.command;

import demo.dto.BookDTO;
import demo.entities.Books;
import org.hibernate.QueryTimeoutException;
import org.hibernate.exception.DataException;
import org.hibernate.exception.JDBCConnectionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import java.util.InputMismatchException;

@RestController
public class addBookCommand {
    @Autowired
    private BookDTO bookDTO;

    @PostMapping(value = "/api/v1/books",consumes = MediaType.APPLICATION_JSON_VALUE)
    public String execute(@RequestBody Books book) {

        System.out.println(book.getIsbn());
        try {
            return bookDTO.addBookAdmin(book.getIsbn(), book.getCount(),book.getAuthor(),book.getName(),book.getDescription());
        }
        catch (JDBCConnectionException jdbc) {
            throw new ResponseStatusException(HttpStatus.GATEWAY_TIMEOUT, "Error connecting to database");
        } catch (InputMismatchException ime) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid input");
        } catch (DataException dataException) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Data error");
        } catch (QueryTimeoutException qte) {
            throw new ResponseStatusException(HttpStatus.GATEWAY_TIMEOUT, "Database connection error");
        }catch (Exception e){
            System.out.println(e.getMessage());
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Error");
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