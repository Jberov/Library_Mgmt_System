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
			result.put("Message", "Потребителят " + userService.deleteUser(username).getUsername() + " е успешно изтрит");
			return ResponseEntity.status(HttpStatus.OK).body(result);
		} catch (NoSuchElementException nsee) {
			result.put("error", "Няма такъв потребител");
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(result);
		} catch (JDBCConnectionException jdbc) {
			result.put("error", "Грешка в системата");
			return ResponseEntity.status(HttpStatus.GATEWAY_TIMEOUT).body(result);
		} catch (IllegalArgumentException illegalArgumentException) {
			result.put("error", illegalArgumentException.getLocalizedMessage());
			return ResponseEntity.status(HttpStatus.CONFLICT).body(result);
		} catch (Exception e) {
			result.put("error", "Системата е временно недостъпна");
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(result);
		}
	}
}
