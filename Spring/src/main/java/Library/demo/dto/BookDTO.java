package Library.demo.dto;

import Library.demo.dao.BooksDAOImpl;
import Library.demo.entities.Books;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.LinkedList;

@Service
public class BookDTO {

    @Autowired
    private BooksDAOImpl booksDAO;

    public LinkedList<Books> getAllBooks(){
        return booksDAO.getAllBooks();
    }

    public boolean addBookAdmin(int count, String name, String author, String description){
        if(!booksDAO.bookExists(name)){
            booksDAO.addBookAdmin(count, name, author, description);
            return true;
        }else{
            return false;
        }
    }

    public String deleteBook(String name){
        if(!booksDAO.bookExists(name)){
            return "No such book found";
        }else{
            booksDAO.deleteBookAdmin(name);
            return "Success";
        }
    }

    public Books getBookById(long isbn){
        if(booksDAO.bookExistsByID(isbn)){
            return booksDAO.getBook(isbn);
        }else{
            return  null;
        }
    }
}
