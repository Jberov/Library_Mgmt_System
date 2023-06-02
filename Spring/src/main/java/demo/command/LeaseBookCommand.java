package demo.command;

import demo.dto.BookDTO;
import demo.services.UserService;
import java.util.InputMismatchException;
import javax.validation.Valid;
import net.minidev.json.JSONObject;
import org.hibernate.exception.JDBCConnectionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class LeaseBookCommand {
	private final UserService userService;
	
	@Autowired
	public LeaseBookCommand(UserService userService) {
		this.userService = userService;
	}
	
	@PatchMapping(value = "api/v1/books/rental/{isbn}")
	public ResponseEntity<JSONObject> execute(@PathVariable("isbn") @Valid String isbn, Authentication authentication) {
		JSONObject result = new JSONObject();
		try {
			BookDTO leased = userService.leaseBook(isbn, authentication.getName());
			if (leased == null) {
				result.put("response", "Book does not exist or is not available");
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body(result);
			}
			
			result.put("message", "Successful lease of the book.");
			result.put("response", leased);
			return ResponseEntity.status(HttpStatus.OK).body(result);
		} catch (JDBCConnectionException jdbc) {
			result.put("error", "Error connecting to database");
			return ResponseEntity.status(HttpStatus.GATEWAY_TIMEOUT).body(result);
		} catch (InputMismatchException ime) {
			result.put("error", "Invalid input");
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);
		} catch (Exception e) {
			result.put("error", "Error, service is currently unavailable");
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(result);
		}
	}
	
	@ExceptionHandler(MethodArgumentNotValidException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ResponseBody
	public ResponseEntity<String> validationError(MethodArgumentNotValidException ex) {
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid isbn number");
	}
}
