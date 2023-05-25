package unitTest;

import demo.LibraryApplication;
import demo.entities.User;
import demo.repositories.UserRepository;
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
public class UserEntityTest {
    
    @Autowired
    private UserRepository userRepository;
    
    @Test
    public void whenFoundByNameReturnBook() {
        User user = new User("JBaller");
        userRepository.save(user);
        User found = userRepository.findByUsername(user.getUsername());
        AssertionsForClassTypes.assertThat(found.getUsername())
                .isEqualTo(user.getUsername());
    }
}
