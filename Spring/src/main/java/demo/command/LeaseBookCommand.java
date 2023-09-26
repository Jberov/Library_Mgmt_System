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
	
	@PatchMapping(value = {"api/v1/books/rental/{isbn}","api/v1/books/rental/{isbn}/{username}"})
	public ResponseEntity<JSONObject> execute(@PathVariable("isbn") @Valid String isbn, Authentication authentication, @PathVariable(value = "username", required = false) String username) {
		JSONObject result = new JSONObject();
		try {
			BookDTO leased;
			if (authentication.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ADMIN") && username != null)) {
				leased = userService.leaseBook(isbn, username);
			} else {
				leased = userService.leaseBook(isbn, authentication.getName());
			}
			if (leased == null) {
				result.put("message", "Книгата не съществува или не е достъпна");
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body(result);
			}
			
			result.put("message", "Успешно заеманe на книгата " + leased.getName());
			return ResponseEntity.status(HttpStatus.OK).body(result);
		} catch (JDBCConnectionException jdbc) {
			result.put("message", "Грешка при достъпа на база данни");
			return ResponseEntity.status(HttpStatus.GATEWAY_TIMEOUT).body(result);
		} catch (InputMismatchException ime) {
			result.put("message", "Невалиден ISBN номер");
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);
		} catch (Exception e) {
			result.put("message", "Грешка, системата е временно недостъпна");
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
