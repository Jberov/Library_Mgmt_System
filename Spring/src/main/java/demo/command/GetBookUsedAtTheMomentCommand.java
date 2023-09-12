package demo.command;

import demo.services.UserService;
import net.minidev.json.JSONObject;
import org.hibernate.exception.JDBCConnectionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.InputMismatchException;


@RestController
public class GetBookUsedAtTheMomentCommand {
	private final UserService userService;
	
	@Autowired
	public GetBookUsedAtTheMomentCommand(UserService userService) {
		this.userService = userService;
	}
	
	@GetMapping(value = "/api/v1/users/{isbn}/books")
	public ResponseEntity<JSONObject> execute(@PathVariable("isbn") @Valid String isbn) {
		JSONObject result = new JSONObject();
		
		try {
			if ((userService.getUsersByBook(isbn) != null) && (!userService.getUsersByBook(isbn).isEmpty())) {
				result.put("users", userService.getUsersByBook(isbn));
				return ResponseEntity.status(HttpStatus.OK).body(result);
			} else if (userService.getUsersByBook(isbn) == null) {
				result.put("message", "Няма такава книга");
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body(result);
			} else if (userService.getUsersByBook(isbn).isEmpty()) {
				result.put("message", "Книгата не е заемана в момента");
				return ResponseEntity.status(HttpStatus.ACCEPTED).body(result);
			} else {
				result.put("message", "Няма такава книга");
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body(result);
			}
		} catch (JDBCConnectionException jdbc) {
			result.put("error", "Проблем в системата");
			return ResponseEntity.status(HttpStatus.GATEWAY_TIMEOUT).body(result);
		} catch (InputMismatchException ime) {
			result.put("error", "Невалидни входни данни");
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);
		} catch (Exception e) {
			result.put("error", "Системата е временно недостъпна");
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(result);
		}
	}
	
	@ExceptionHandler(MethodArgumentNotValidException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ResponseBody
	public ResponseEntity<String> validationError(MethodArgumentNotValidException ex) {
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Невалиден ISBN номер");
	}
	
}
