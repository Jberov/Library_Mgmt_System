package demo.command;

import demo.services.UserService;
import net.minidev.json.JSONObject;
import org.hibernate.exception.JDBCConnectionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.NoSuchElementException;

@RestController
public class DeleteUserCommand {
	private final UserService userService;
	
	@Autowired
	public DeleteUserCommand(UserService userService) {
		this.userService = userService;
	}
	
	@DeleteMapping(value = "api/v1/users/{username}")
	public ResponseEntity<JSONObject> execute(@PathVariable("username") String username){
		JSONObject result = new JSONObject();
		try {
			result.put("Message", "User successfully deleted");
			result.put("Deleted user", userService.deleteUser(username));
			return ResponseEntity.status(HttpStatus.OK).body(result);
		} catch (NoSuchElementException nsee) {
			result.put("error", "No such user");
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(result);
		} catch (JDBCConnectionException jdbc) {
			result.put("error", "Error connecting to database");
			return ResponseEntity.status(HttpStatus.GATEWAY_TIMEOUT).body(result);
		}catch (IllegalArgumentException illegalArgumentException) {
			result.put("error", illegalArgumentException.getLocalizedMessage());
			return ResponseEntity.status(HttpStatus.CONFLICT).body(result);
		} catch (Exception e) {
			result.put("error", "Error, service is currently unavailable");
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(result);
		}
	}
}
