package unitTests;

import com.sap.cloud.security.xsuaa.XsuaaServiceConfiguration;
import demo.LibraryApplication;
import demo.entities.Book;
import demo.repositories.BookRepository;
import org.assertj.core.api.AssertionsForClassTypes;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {
        LibraryApplication.class,
        XsuaaServiceConfiguration.class})
public class BookEntityTest {
    private final BookRepository bookRepository;
    
    @Autowired
    public BookEntityTest(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }
    
    @Test
    public void whenFoundByNameReturnBook() {
        Book book = new Book("9780141301068", 3, "Roald Dahl", "Matilda", "Genius girl", true);
        bookRepository.save(book);
        Book found = bookRepository.findByName(book.getName());
        AssertionsForClassTypes.assertThat(found.getAuthor())
                .isEqualTo(book.getAuthor());
    }
}