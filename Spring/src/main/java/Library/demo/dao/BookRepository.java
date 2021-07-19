package Library.demo.dao;

import Library.demo.entities.Books;
import org.springframework.data.jpa.repository.JpaRepository;


import org.springframework.stereotype.Repository;



@Repository
public interface BookRepository extends JpaRepository<Books, Long> {

    Books findByName(String name);

}
