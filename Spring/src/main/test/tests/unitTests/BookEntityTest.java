package tests.unitTests;

import demo.repositories.BookRepository;
import demo.entities.Books;

import org.assertj.core.api.AssertionsForClassTypes;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.context.SpringBootTest;
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
        Books book = new Books("978-06-79826-62-9",3, "Roald Dahl", "Matilda", "Genius girl",true);
        entityManager.persistAndFlush(book);
        Books found = bookRepository.findByName(book.getName());

        AssertionsForClassTypes.assertThat(found.getAuthor())
                .isEqualTo(book.getAuthor());
    }

}