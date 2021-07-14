package Library.demo.dao;

import Library.demo.entities.Books;
import org.hibernate.QueryTimeoutException;
import org.hibernate.exception.ConstraintViolationException;
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
        try {
            LinkedList<Books> books = new LinkedList<>();

            for (Books book : bookRepository.findAll()) {
                books.add(book);
            }
            System.out.println(bookRepository.count());
            return books;
        }catch (JDBCConnectionException jdbcConnectionException){
            throw new ResponseStatusException(HttpStatus.GONE, "Database connection error");
        }catch(DataException dataException){
            throw new ResponseStatusException(HttpStatus.GATEWAY_TIMEOUT, "Database connection error");
        }catch(Exception e){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Error");
        }
    }
    public void addBookAdmin(int count, String name, String author, String description) throws ResponseStatusException{
        try {
            Books book = new Books(count, name, author, description);
            for (Books record : bookRepository.findAll()) {
                if (record.getName().equals(book.getName())) {
                    throw new ResponseStatusException(HttpStatus.CONFLICT, "Such book already exists");
                }
            }
            bookRepository.save(book);
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

