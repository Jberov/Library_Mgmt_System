package demo.repositories;

import demo.entities.BooksActivity;
import demo.entities.Status;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.LinkedList;

public interface BookRecordsRepository extends JpaRepository<BooksActivity, Long> {
    BooksActivity findByBooksIsbnAndStatusAndUserId(long isbn, Status status, long userId);
    LinkedList<BooksActivity> findByUserId(long userID);
    LinkedList<BooksActivity> findByBooksIsbnAndStatus(long userID, Status status);
    boolean existsByBooksIsbnAndStatus(long userID,Status status);
}
