package demo.repositories;

import demo.entities.Book;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRolesRepository extends JpaRepository<Book, String> {

}
