package demo.repositories;

import demo.entities.BooksActivity;
import demo.enums.Status;
import java.time.LocalDate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface BookRecordsRepository extends JpaRepository<BooksActivity, Long> {
	BooksActivity findFirstByBookIsbnAndStatusAndUserId(String isbn, Status status, long userId);
	
	List<BooksActivity> findByUserId(long userID);
	
	Set<BooksActivity> findByBookIsbnAndStatus(String isbn, Status status);
	
	List<BooksActivity> findByBookIsbn(String isbn);
	
	boolean existsByBookIsbnAndStatus(String isbn, Status status);

	@Query("FROM BooksActivity ba WHERE ba.issueDate >= ?1")
	List<BooksActivity> findByDate(LocalDate date);

}
