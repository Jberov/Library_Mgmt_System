package demo.repositories;

import demo.entities.BooksActivity;
import demo.status.Status;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.LinkedList;

public interface BookRecordsRepository extends JpaRepository<BooksActivity, Long> {
	BooksActivity findFirstByBookIsbnAndStatusAndUserId(String isbn, Status status, long userId);
	
	LinkedList<BooksActivity> findByUserId(long userID);
	
	LinkedList<BooksActivity> findByBookIsbnAndStatus(String isbn, Status status);
	
	boolean existsByBookIsbnAndStatus(String isbn, Status status);
}