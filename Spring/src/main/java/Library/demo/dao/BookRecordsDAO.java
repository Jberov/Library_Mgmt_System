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
        new BooksActivity();
        BooksActivity temp;
        temp = bookRecordsRepository.findByBooksIsbnAndStatusAndUserId(books.getBook(bookId).getIsbn(),Status.TAKEN,user.findUserByName(username).getId());
        temp.setStatus(Status.RETURNED);
        books.increaseCount(bookId);
        bookRecordsRepository.save(temp);
        return "Book successfully returned";
    }
    public LinkedList<LinkedList<Books>> booksUsedByUser(String username){
        LinkedList<LinkedList<Books>> books = new LinkedList<>();
        LinkedList<BooksActivity> records = bookRecordsRepository.findByUserId(userRepository.findByName(username).getId());
        LinkedList<Books> takenBooks = new LinkedList<>();
        LinkedList<Books> returnedBooks = new LinkedList<>();
        books.add(takenBooks);
        books.add(returnedBooks);
        for(BooksActivity i : records){
            if(i.getStatus().equals(Status.TAKEN)){
                books.get(0).add(i.getBook());
            }else{
                books.get(1).add(i.getBook());
            }
        }
        return books;
    }

    }

