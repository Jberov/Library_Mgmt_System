package demo.command;

import demo.services.BookService;
import net.minidev.json.JSONObject;
import org.hibernate.exception.JDBCConnectionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.InputMismatchException;

@RequestMapping("/api/v1")
@RestController
public class GetBookCommand {
	private final BookService bookService;
	
	@Autowired
	public GetBookCommand(BookService bookService) {
		this.bookService = bookService;
	}
	
	
	@GetMapping(value = "/books/{isbn}")
	public ResponseEntity<JSONObject> getBook(@PathVariable("isbn") @Valid String isbn) {
		JSONObject result = new JSONObject();
		
		try {
			if (bookService.getBookById(isbn) != null) {
				result.put("book", bookService.getBookById(isbn));
				return ResponseEntity.status(HttpStatus.OK).body(result);
			}
			
			result.put("error", "No such book found");
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(result);
			
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
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ResponseBody
	public ResponseEntity<String> validationError(MethodArgumentNotValidException ex) {
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid isbn number");
	}
}