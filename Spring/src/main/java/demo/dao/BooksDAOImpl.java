package demo.dao;

import demo.entities.Books;
import demo.repositories.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.LinkedList;

@Service
public class BooksDAOImpl {
    @Autowired
    BookRepository bookRepository;

    public LinkedList<Books> getAllBooks() throws ResponseStatusException{
        return new LinkedList<>(bookRepository.findByExistence(true));
    }
    public String addBookAdmin(int count, String name, String author, String description) throws ResponseStatusException{
        Books book = new Books(count, name, author, description, true);
        bookRepository.save(book);
        return "Success";

    }

    public String deleteBookAdmin(String name){
        Books temp = bookRepository.findByName(name);
        temp.setExists(false);
        temp.setCount(0);
        bookRepository.save(temp);
        return "Delete successful";
    }
    public Books getBook(long isbn){
        if(bookRepository.findById(isbn).isPresent() && bookRepository.findById(isbn).get().isExists()){
            return bookRepository.findById(isbn).get();
        }else{
            return null;
        }
    }
    public Books getBookByName(String name){
        return bookRepository.findByName(name);
    }
    public boolean bookExistsByID(long isbn){
        return bookRepository.findById(isbn).isPresent();
    }
    public boolean doesBookExist(String name){
        return bookRepository.findByName(name) != null;
    }
    public void decreaseCount(long bookId){
        Books temp = bookRepository.getById(bookId);
        temp.setCount(temp.getCount() - 1);
        bookRepository.save(temp);
    }
    public void increaseCount(long bookId){
        Books temp = bookRepository.getById(bookId);
        temp.setCount(temp.getCount() + 1);
        bookRepository.save(temp);
    }
    public int checkCount(long isbn){
        return bookRepository.findById(isbn).get().getCount();
    }
}

