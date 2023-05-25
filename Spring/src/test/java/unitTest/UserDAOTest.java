package unitTest;

import demo.LibraryApplication;
import demo.dao.UserDAOImpl;
import demo.entities.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ActiveProfiles("test")
@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = {
		LibraryApplication.class})
@ContextConfiguration(classes = LibraryApplication.class)
public class UserDAOTest {
	private static final String username = "";
	
	private static final User user = new User(username);

	@MockBean
	private UserDAOImpl userDAO;

	
	@Test
	public void getAndDeleteUserTest() {
		BDDMockito.given(userDAO.findUserByName(username)).willReturn(user);
		Assertions.assertEquals(userDAO.findUserByName(username).getUsername(), user.getUsername());
		BDDMockito.given(userDAO.deleteUser(username)).willReturn(user);
		Assertions.assertEquals(userDAO.findUserByName(username).getUsername(), user.getUsername());
	}
	
	@Test
	public void userExistsTest() {
		BDDMockito.given(userDAO.findUserByName(username)).willReturn(user);
		Assertions.assertEquals(userDAO.findUserByName(username).getUsername(), user.getUsername());
	}
	
	@Test
	public void isUserTest() {
		BDDMockito.given(userDAO.isUser(username)).willReturn(true);
		Assertions.assertTrue(userDAO.isUser(username));
	}
}
