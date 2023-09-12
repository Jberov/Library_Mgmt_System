package demo.command;

import demo.dto.BookDTO;
import demo.services.UserService;
import net.minidev.json.JSONObject;
import org.hibernate.exception.JDBCConnectionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.InputMismatchException;


@RestController
public class ReturnBookCommand {
	private final UserService userService;
	
	@Autowired
	public ReturnBookCommand(UserService userService) {
		this.userService = userService;
	}
	
	@PatchMapping(value = "api/v1/books/reconveyance/{isbn}")
	public ResponseEntity<JSONObject> execute(@PathVariable("isbn") @Valid String isbn, Authentication authentication) {
		
		JSONObject result = new JSONObject();
		
		try {
			BookDTO returned = userService.returnBook(isbn, authentication.getName());
			if (returned == null) {
				result.put("message", "No such book exists or has been taken by you");
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body(result);
			}
			
			result.put("message", "Успешно върната книга");
			return ResponseEntity.status(HttpStatus.OK).body(result);
		} catch (JDBCConnectionException jdbc) {
			result.put("error", "Грешка при връзка с базата данни");
			return ResponseEntity.status(HttpStatus.GATEWAY_TIMEOUT).body(result);
		} catch (InputMismatchException ime) {
			result.put("error", "Невалидни данни");
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);
		} catch (Exception e) {
			result.put("error", "Системата е временно недостъпна");
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(result);
		}
	}
	
	@ExceptionHandler(MethodArgumentNotValidException.class)
	@ResponseBody
	public ResponseEntity<String> validationError(MethodArgumentNotValidException ex) {
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid isbn number");
	}
}
