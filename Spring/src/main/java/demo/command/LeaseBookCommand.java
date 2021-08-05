package demo.command;

import demo.dto.UserDTO;
import org.hibernate.QueryTimeoutException;
import org.hibernate.exception.DataException;
import org.hibernate.exception.JDBCConnectionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.*;

import java.util.InputMismatchException;
import java.util.NoSuchElementException;


@RestController
public class LeaseBookCommand {
    @Autowired
    private UserDTO userDTO;

    @PatchMapping("/users/lease")
    public String leaseBook(@RequestParam long isbn, @RequestParam String username){
        try{
            return userDTO.leaseBook(isbn, username);
        }catch (JDBCConnectionException jdbc){
            return "Error connecting to database";
        }catch (InputMismatchException ime){
            return "Invalid or incomplete input";
        }catch(DataException dataException){
           return "Data error";
        }catch(QueryTimeoutException qte){
            return "Database connection error";
        }catch (NoSuchElementException nsee){
            return "No book with this id exists";
        }catch (NullPointerException nptr){
            return "No such book";
        }catch (Exception e){
            System.out.println(e.getMessage());
            return "Error";
        }
    }
    @ExceptionHandler(MissingServletRequestParameterException.class)
    public String handleMissingParams(MissingServletRequestParameterException ex) {
        return ex.getParameterName() + " parameter is missing";
    }
}
