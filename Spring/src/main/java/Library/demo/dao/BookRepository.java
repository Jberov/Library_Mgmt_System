package Library.demo.dao;

import Library.demo.entities.Books;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface BookRepository extends JpaRepository<Books, Long> {
    @Query("FROM BOOKS u WHERE u.name = :name")
    Books getBookByName(
            @Param(value = "name") String name
    );
}
