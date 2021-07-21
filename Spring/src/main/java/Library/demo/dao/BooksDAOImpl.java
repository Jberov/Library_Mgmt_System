package Library.demo.dao;

import Library.demo.entities.Books;
import Library.demo.repositories.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

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
        if(bookRepository.findById(isbn).isPresent()){
            return bookRepository.findById(isbn).get();
        }else{
            return null;
        }
    }
    public boolean bookExistsByID(long isbn){
        return bookRepository.findById(isbn).isPresent();
    }
    public boolean bookExists(String name){
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

