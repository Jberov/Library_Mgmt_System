package demo.command;

import demo.dto.BookDTO;
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
public class ReturnBookCommand {
	private final UserService userService;
	
	@Autowired
	public ReturnBookCommand(UserService userService) {
		this.userService = userService;
	}
	
	@PatchMapping(value = "api/v1/books/reconveyance/{isbn}")
	public ResponseEntity<JSONObject> execute(@PathVariable("isbn") @Valid String isbn) {
		
		JSONObject result = new JSONObject();
		
		try {
			BookDTO returned = userService.returnBook(isbn, "");
			if (returned == null) {
				result.put("message", "No such book exists or has been taken by you");
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body(result);
			}
			
			result.put("message", "Book successfully returned");
			result.put("response", returned);
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
	
	@ExceptionHandler(MethodArgumentNotValidException.class)
	@ResponseBody
	public ResponseEntity<String> validationError(MethodArgumentNotValidException ex) {
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid isbn number");
	}
}
