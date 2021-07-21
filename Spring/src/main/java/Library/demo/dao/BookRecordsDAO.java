package Library.demo.dao;

import Library.demo.entities.Books;
import Library.demo.entities.BooksActivity;
import Library.demo.entities.Status;
import Library.demo.repositories.BookRecordsRepository;
import Library.demo.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.LinkedList;

@Service
public class BookRecordsDAO {
    @Autowired
    private BookRecordsRepository bookRecordsRepository;
    @Autowired
    private BooksDAOImpl books;
    @Autowired
    private UserDAOImpl user;
    @Autowired
    private UserRepository userRepository;

    public String leaseBook(long bookId, String userName){
        BooksActivity activity = new BooksActivity(user.findUserByName(userName),books.getBook(bookId), Status.TAKEN);
        books.decreaseCount(bookId);
        bookRecordsRepository.save(activity);
        System.out.println(bookRecordsRepository.findByUserId(user.findUserByName(userName).getId()).get(0).getBook().getName());
        return "Book successfully leased";


    }
    public String returnBook(long bookId, String username){
        LinkedList <BooksActivity> test = bookRecordsRepository.findByUserId(user.findUserByName(username).getId());
        for(BooksActivity i: test){
            System.out.println(i.getStatus());
        }
        new BooksActivity();
        BooksActivity temp;
        temp = bookRecordsRepository.findByBooksIsbnAndStatusAndUserId(books.getBook(bookId).getIsbn(),Status.TAKEN,user.findUserByName(username).getId());
        System.out.println(temp.getBook().getName());
        temp.setStatus(Status.RETURNED);
        books.increaseCount(bookId);
        bookRecordsRepository.save(temp);
        return "Book successfully returned";
    }
    public LinkedList<Books> booksUsedByUser(String username){
        LinkedList<Books> books = new LinkedList<>();
        LinkedList<BooksActivity> records = bookRecordsRepository.findByUserId(userRepository.findByName(username).getId());
        for(BooksActivity i : records){
            books.add(i.getBook());
        }
        return books;
    }

    }
    //private Books recursiveIterator()

