package demo.command;

import com.sap.cloud.security.xsuaa.token.SpringSecurityContext;
import demo.services.UserService;
import net.minidev.json.JSONObject;
import org.hibernate.exception.JDBCConnectionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.InputMismatchException;
import java.util.List;
import java.util.Map;

@RestController
public class UserHistoryCommand {
	private final UserService userService;
	
	@Autowired
	public UserHistoryCommand(UserService userService) {
		this.userService = userService;
	}
	
	@GetMapping(value = "api/v1/users/history")
	public ResponseEntity<JSONObject> execute() {
		JSONObject result = new JSONObject();
		Map<String, List<String>> history = userService.userUsedBooks(SpringSecurityContext.getToken().getLogonName());
		try {
			if (history == null) {
				result.put("error", "No such user");
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body(result);
			}
			
			result.put(SpringSecurityContext.getToken().getLogonName() + "'s history", history);
			return ResponseEntity.status(HttpStatus.OK).body(result);
		} catch (JDBCConnectionException jdbc) {
			result.put("error", "Error connecting to database");
			return ResponseEntity.status(HttpStatus.GATEWAY_TIMEOUT).body(result);
		} catch (InputMismatchException ime) {
			result.put("error", "Invalid input");
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);
		} catch (Exception e) {
			System.out.println(e.getMessage());
			result.put("error", "Error, service is currently unavailable");
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(result);
		}
	}
	
	@ExceptionHandler(MissingServletRequestParameterException.class)
	public ResponseEntity<String> handleMissingParams(MissingServletRequestParameterException ex) {
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Missing parameter(s): " + ex.getParameterName());
	}
	
}
