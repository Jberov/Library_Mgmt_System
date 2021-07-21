package Library.demo.command;

import Library.demo.dto.UserDTO;
import org.hibernate.QueryTimeoutException;
import org.hibernate.exception.DataException;
import org.hibernate.exception.JDBCConnectionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

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
        }catch (Exception e){
            System.out.println(e.getMessage());
            return "Error";
        }
    }
}
