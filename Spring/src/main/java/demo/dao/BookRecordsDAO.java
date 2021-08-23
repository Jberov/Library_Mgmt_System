package demo.dao;

import demo.entities.Books;
import demo.entities.BooksActivity;
import demo.entities.Status;
import demo.repositories.BookRecordsRepository;
import demo.repositories.UserRepository;
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

    public String leaseBook(String bookId, String userName){
        if(books.getBook(bookId).isExists()){
            BooksActivity activity = new BooksActivity(user.findUserByName(userName),books.getBook(bookId), Status.TAKEN);
            books.decreaseCount(bookId);
            bookRecordsRepository.save(activity);
            return "Book successfully leased";
        }else{
            return "Book no longer exists";
        }
    }

    public boolean checkIfUserHasTakenBook(String isbn, String username){
        for(String i: getUsersByBook(isbn)){
            if(i.equals(username)){
                return true;
            }
        }
        return false;
    }

    public boolean userHistoryExists(String isbn){
        return bookRecordsRepository.existsByBooksIsbnAndStatus(isbn, Status.TAKEN);
    }

    public String returnBook(String bookId, String username){

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

    public LinkedList<String> getUsersByBook(String bookId) {
        if(books.getBook(bookId).isExists()){
            LinkedList<String> usernames = new LinkedList<>();
            LinkedList<BooksActivity> records = bookRecordsRepository.findByBooksIsbnAndStatus(books.getBook(bookId).getIsbn(), Status.TAKEN);
            for (BooksActivity i : records) {
                usernames.add(i.getUser().getName());
            }
            return usernames;
        }else{
            return null;
        }
    }
}

