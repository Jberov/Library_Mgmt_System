package demo.command;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import demo.dto.UserDTO;
import demo.services.StatisticsService;
import demo.services.UserService;
import java.util.InputMismatchException;
import net.minidev.json.JSONObject;
import org.hibernate.exception.JDBCConnectionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/v1/users")
@RestController
public class GetUserCommand {
	private final UserService userService;
	private final StatisticsService service;
	private final ObjectMapper mapper = new ObjectMapper();
	
	@Autowired
	public GetUserCommand(UserService userService, StatisticsService service) {
		this.userService = userService;
		this.service = service;
	}
	
	@GetMapping(value = {"/info/single/{name}", "/info/single"}, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<JSONObject> execute(@PathVariable(value = "name", required = false) String name, Authentication auth) {
		JSONObject result = new JSONObject();
		try {

			UserDTO user;
			if (name == null){
				user = userService.getUser(auth.getName());
			} else {
				user = userService.getUser(name);
			}

			if (user == null) {
				result.put("error", "No such user");
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body(result);
			}
			
			result.put("user", user);
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

	@GetMapping(value = "/info", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<JsonNode> executeAllUsers() {
			return ResponseEntity.status(HttpStatus.OK).body(mapper.convertValue(userService.getAllUsers(), JsonNode.class));
	}

	@GetMapping(path = "/recommendation",produces = MediaType.TEXT_PLAIN_VALUE)
	public ResponseEntity<String> getRecommendation(Authentication authentication){
		return ResponseEntity.status(HttpStatus.OK).body(service.suggestBookToUser(authentication.getName()));
	}

	
	@ExceptionHandler(MissingServletRequestParameterException.class)
	public ResponseEntity<String> handleMissingParams(MissingServletRequestParameterException ex) {
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Missing parameter(s): " + ex.getParameterName());
	}

	@ExceptionHandler(JDBCConnectionException.class)
	public ResponseEntity<String> handleDBError(JDBCConnectionException ex) {
		return ResponseEntity.status(HttpStatus.GATEWAY_TIMEOUT).body("DB problems");
	}

	@ExceptionHandler(NullPointerException.class)
	public ResponseEntity<String> handleNullPtr(NullPointerException ex) {
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No users");
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<String> generalExceptionHandler(Exception ex) {
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage());
	}
}
