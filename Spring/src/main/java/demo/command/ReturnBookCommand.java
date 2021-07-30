package demo.command;

import demo.dto.UserDTO;
import org.hibernate.QueryTimeoutException;
import org.hibernate.exception.DataException;
import org.hibernate.exception.JDBCConnectionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.InputMismatchException;

@RestController
public class ReturnBookCommand {
    @Autowired
    private UserDTO userDTO;

    @PatchMapping("/users/returnBook")
    public String returnBook(@RequestParam long isbn, @RequestParam String username){
        try{
            return userDTO.returnBook(isbn, username);
        }catch (JDBCConnectionException jdbc){
            System.out.println(jdbc.getMessage());
           return "Error connecting to database";
        }catch (InputMismatchException ime){
            System.out.println(ime.getMessage());
           return "Invalid input";
        }catch(DataException dataException){
            System.out.println(dataException.getMessage());
            return "Data error";
        }catch(QueryTimeoutException qte){
            System.out.println(qte.getMessage());
            return "Database connection error";
        }catch(NullPointerException nullPointerException){
            System.out.println(nullPointerException.getMessage());
            return "No such user or book";
        }catch (Exception e){
            System.out.println(e.getMessage());
            return  "Error";
        }
    }

}
