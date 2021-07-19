package Library.demo.dao;

import Library.demo.entities.Books;
import org.hibernate.QueryTimeoutException;
import org.hibernate.exception.DataException;
import org.hibernate.exception.JDBCConnectionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.InputMismatchException;
import java.util.LinkedList;

@Service
public class BooksDAOImpl {
    @Autowired
    BookRepository bookRepository;

    public LinkedList<Books> getAllBooks() throws ResponseStatusException{
        return new LinkedList<>(bookRepository.findAll());
    }
    public String addBookAdmin(int count, String name, String author, String description) throws ResponseStatusException{
        Books book = new Books(count, name, author, description);
        bookRepository.save(book);
        return "Success";

    }

    public String deleteBookAdmin(String name){
        bookRepository.deleteById(bookRepository.findByName(name).getIsbn());
        return "Delete successful";
    }
    public Books getBook(long isbn){
        try{
            return bookRepository.findById(isbn).get();
        }catch (JDBCConnectionException jdbc){
            throw new ResponseStatusException(HttpStatus.GATEWAY_TIMEOUT, "Error connecting to database");
        }catch (InputMismatchException ime){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid input");
        }catch(DataException dataException){
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Data error");
        }catch(QueryTimeoutException qte){
            throw new ResponseStatusException(HttpStatus.GATEWAY_TIMEOUT, "Database connection error");
        }
    }
}

