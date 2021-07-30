package demo.command;


import demo.dto.BookDTO;
import org.hibernate.QueryTimeoutException;
import org.hibernate.exception.DataException;
import org.hibernate.exception.JDBCConnectionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.*;

import java.util.InputMismatchException;

@CrossOrigin
@RestController
public class DeleteBookAdminCommand {
    @Autowired
    BookDTO bookDTO;

    @PatchMapping("/admin/books/delete")
    public String execute(@RequestParam String name){
        try{
            return bookDTO.deleteBook(name);
        }catch (JDBCConnectionException jdbc){
            return "Error connecting to database";
        }catch (InputMismatchException ime){
            return "Invalid input";
        }catch(DataException dataException){
            return "Data error";
        }catch(QueryTimeoutException qte){
            return "Database connection error";
        }catch (Exception e){
            System.out.println(e.getMessage());
            return "Error";
        }
    }
}
