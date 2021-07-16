package Library.demo;

import Library.demo.dao.BookRepository;
import Library.demo.entities.Books;

import org.assertj.core.api.AssertionsForClassTypes;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@DataJpaTest
public class BookEntityTest {
    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private BookRepository bookRepository;

    @Test
    public void whenFoundByNameReturnBook(){
        Books book = new Books(3, "Roald Dahl", "Matilda", "Genius girl");
        entityManager.persist(book);
        entityManager.flush();

        Books found = bookRepository.findByName(book.getName());

        AssertionsForClassTypes.assertThat(found.getAuthor())
                .isEqualTo(book.getAuthor());
    }

}
