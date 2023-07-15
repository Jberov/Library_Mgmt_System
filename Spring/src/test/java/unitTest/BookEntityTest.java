package unitTest;

import demo.LibraryApplication;
import demo.entities.Author;
import demo.entities.Book;
import demo.entities.Genre;
import demo.repositories.BookRepository;
import org.assertj.core.api.AssertionsForClassTypes;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ActiveProfiles("test")
@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = {
        LibraryApplication.class})
@ContextConfiguration(classes = LibraryApplication.class)
public class BookEntityTest {

    @Autowired
    private BookRepository bookRepository;

    @Test
    public void whenFoundByNameReturnBook() {
        Book book = new Book("978-0-09-959008-8",7,new Author("Харари", "Еврейски публицист"),"Кратка история на Хомо Сапиенс","История", new Genre("Публицистика", "Описание"));
        bookRepository.save(book);
        Book found = bookRepository.findByName(book.getName());
        AssertionsForClassTypes.assertThat(found.getAuthor())
                .isEqualTo(book.getAuthor());
    }
}
