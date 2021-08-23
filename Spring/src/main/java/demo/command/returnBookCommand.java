package demo.command;

import demo.dto.UserDTO;
import org.hibernate.QueryTimeoutException;
import org.hibernate.exception.DataException;
import org.hibernate.exception.JDBCConnectionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.util.InputMismatchException;


@RestController
public class returnBookCommand {
    @Autowired
    private UserDTO userDTO;

    @PatchMapping(value = "api/v1/books/return/{isbn}&{username}")
    public String returnBook(@PathVariable("isbn") @Valid String isbn, @PathVariable("username") String username){
        try{
            return userDTO.returnBook(isbn, username);
        }catch (JDBCConnectionException jdbc){
            System.out.println(jdbc.getMessage());
            throw new ResponseStatusException(HttpStatus.GATEWAY_TIMEOUT, "Error connecting to database");
        }catch (InputMismatchException ime){
            System.out.println(ime.getMessage());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid input");
        }catch(DataException dataException){
            System.out.println(dataException.getMessage());
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Data error");
        }catch(QueryTimeoutException qte){
            System.out.println(qte.getMessage());
            throw new ResponseStatusException(HttpStatus.GATEWAY_TIMEOUT, "Database connection error");
        }catch(NullPointerException nullPointerException){
            System.out.println(nullPointerException.getMessage());
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No records for this user or book");
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
