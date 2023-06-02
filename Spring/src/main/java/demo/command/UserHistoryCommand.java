package demo.command;

import demo.services.UserService;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Map;
import net.minidev.json.JSONObject;
import org.hibernate.exception.JDBCConnectionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserHistoryCommand {
	private final UserService userService;
	
	@Autowired
	public UserHistoryCommand(UserService userService) {
		this.userService = userService;
	}
	
	@GetMapping(value = "api/v1/users/history/{username}")
	public ResponseEntity<JSONObject> execute(Authentication authentication, @PathVariable(required = false) String username) {
		Map<String, List<String>> history;
		JSONObject result = new JSONObject();
		String endUser;
		if (authentication.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ADMIN")) && username != null){
			System.out.println("God-Emperor mode");
			endUser = username;
		} else {
			System.out.println("Peasant mode");
			endUser = authentication.getName();
		}
		history = userService.userUsedBooks(endUser);

		try {
			if (history == null) {
				result.put("error", "No such user");
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body(result);
			}
			
			result.put(endUser + "'s history", history);
			return ResponseEntity.status(HttpStatus.OK).body(result);
		} catch (JDBCConnectionException jdbc) {
			result.put("error", "Error connecting to database");
			return ResponseEntity.status(HttpStatus.GATEWAY_TIMEOUT).body(result);
		} catch (InputMismatchException ime) {
			result.put("error", "Invalid input");
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);
		} catch (Exception e) {
			result.put("error", "Error, service is currently unavailable " + e.getMessage());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(result);
		}
	}
	
	@ExceptionHandler(MissingServletRequestParameterException.class)
	public ResponseEntity<String> handleMissingParams(MissingServletRequestParameterException ex) {
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Missing parameter(s): " + ex.getParameterName());
	}
	
}
