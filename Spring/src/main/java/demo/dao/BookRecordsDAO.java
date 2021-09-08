package demo.dao;

import demo.entities.Books;
import demo.entities.BooksActivity;
import demo.status.Status;
import demo.repositories.BookRecordsRepository;
import demo.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
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

    public Books leaseBook (String bookId, String userName) {
        if (books.getBook(bookId) != null) {
            BooksActivity activity = new BooksActivity(user.findUserByName(userName),books.getBook(bookId), Status.TAKEN);
            books.decreaseCount(bookId);
            bookRecordsRepository.save(activity);
            return books.getBook(bookId);
        }
        return null;
    }

    public boolean checkIfUserHasTakenBook (String isbn, String username) {
        if(getUsersByBook(isbn) == null){
            return false;
        }
        for (String name: getUsersByBook(isbn)) {
            if (name.equals(username)) {
                return true;
            }
        }
        return false;
    }

    public boolean userHistoryExists (String isbn) {
        return bookRecordsRepository.existsByBooksIsbnAndStatus(isbn, Status.TAKEN);
    }

    public Books returnBook (String bookId, String username) {
        new BooksActivity();
        BooksActivity temp;
        temp = bookRecordsRepository.findFirstByBooksIsbnAndStatusAndUserId(books.getBook(bookId).getIsbn(),Status.TAKEN,user.findUserByName(username).getId());
        temp.setStatus(Status.RETURNED);
        books.increaseCountBy1(bookId);
        bookRecordsRepository.save(temp);
        return books.getBook(bookId);
    }

    public HashMap<String,LinkedList<Books>> booksUsedByUser (String username) {
        HashMap<String,LinkedList<Books>> books = new HashMap<>();
        LinkedList<BooksActivity> records = bookRecordsRepository.findByUserId(userRepository.findByName(username).getId());
        if(records == null){
            return null;
        }
        LinkedList<Books> takenBooks = new LinkedList<>();
        LinkedList<Books> returnedBooks = new LinkedList<>();
        books.put("Currently taken books by user:",takenBooks);
        books.put("Already returned books by user:",returnedBooks);
        for (BooksActivity record : records) {
            if (record.getStatus().equals(Status.TAKEN)) {
                books.get("Currently taken books by user:").add(record.getBook());
            } else {
                books.get("Already returned books by user:").add(record.getBook());
            }
        }
        return books;
    }

    public LinkedList<String> getUsersByBook (String bookId) {
        if (books.getBook(bookId) != null) {
            LinkedList<String> usernames = new LinkedList<>();
            LinkedList<BooksActivity> records = bookRecordsRepository.findByBooksIsbnAndStatus(books.getBook(bookId).getIsbn(), Status.TAKEN);
            for (BooksActivity record : records) {
                usernames.add(record.getUser().getName());
            }
            return usernames;
        }
        return null;
    }
}

