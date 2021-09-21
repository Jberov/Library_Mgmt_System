package unitTests;

import com.sap.cloud.security.xsuaa.XsuaaServiceConfiguration;
import demo.LibraryApplication;
import demo.entities.User;
import demo.repositories.UserRepository;
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
public class UserEntityTest {
    
    @Autowired
    private UserRepository userRepository;
    
    @Test
    public void whenFoundByNameReturnBook() {
        User user = new User("JBaller");
        userRepository.save(user);
        User found = userRepository.findByName(user.getName());
        AssertionsForClassTypes.assertThat(found.getName())
                .isEqualTo(user.getName());
    }
}