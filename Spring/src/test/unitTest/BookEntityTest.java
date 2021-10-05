package unitTest;

import com.sap.cloud.security.xsuaa.XsuaaServiceConfiguration;
import demo.LibraryApplication;
import demo.entities.Book;
import demo.repositories.BookRepository;
import org.assertj.core.api.AssertionsForClassTypes;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ActiveProfiles("test")
@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = {
        LibraryApplication.class,
        XsuaaServiceConfiguration.class})
public class BookEntityTest {

    @Autowired
    private BookRepository bookRepository;

    @Test
    public void whenFoundByNameReturnBook() {
        Book book = new Book("9780141301068", 3, "Roald Dahl", "Matilda", "Genius girl");
        bookRepository.save(book);
        Book found = bookRepository.findByName(book.getName());
        AssertionsForClassTypes.assertThat(found.getAuthor())
                .isEqualTo(book.getAuthor());
    }
}