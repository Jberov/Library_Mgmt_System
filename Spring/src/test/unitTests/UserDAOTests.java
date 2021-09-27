package unitTests;

import com.sap.cloud.security.xsuaa.XsuaaServiceConfiguration;
import demo.LibraryApplication;
import demo.dao.UserDAOImpl;
import demo.entities.User;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.mockito.BDDMockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {
		LibraryApplication.class,
		XsuaaServiceConfiguration.class})
public class UserDAOTests {
	private static final String username = "";
	
	private static final User user = new User(username);

	@MockBean
	private UserDAOImpl userDAO;

	
	@Test
	public void getUserTest() {
		BDDMockito.given(userDAO.findUserByName(username)).willReturn(user);
		Assertions.assertEquals(userDAO.findUserByName(username).getName(), user.getName());
	}
	
	@Test
	public void userExistsTest() {
		BDDMockito.given(userDAO.findUserByName(username)).willReturn(user);
		Assertions.assertEquals(userDAO.findUserByName(username).getName(), user.getName());
	}
	
	@Test
	public void deleteUserTest() {
		BDDMockito.given(userDAO.deleteUser(username)).willReturn(user);
		Assertions.assertEquals(userDAO.findUserByName(username).getName(), user.getName());
	}
	
	@Test
	public void isUserTest() {
		BDDMockito.given(userDAO.isUser(username)).willReturn(true);
		Assertions.assertTrue(userDAO.isUser(username));
	}
}
