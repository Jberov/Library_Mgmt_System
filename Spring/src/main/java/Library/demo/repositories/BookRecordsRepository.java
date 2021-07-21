package Library.demo.repositories;

import Library.demo.entities.BooksActivity;
import Library.demo.entities.Status;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.LinkedList;

public interface BookRecordsRepository extends JpaRepository<BooksActivity, Long> {
    BooksActivity findByBooksIsbnAndStatusAndUserId(long isbn, Status status, long userId);
    LinkedList<BooksActivity> findByUserId(long userID);
}
