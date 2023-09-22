package demo.command;

import demo.dto.BookDTO;
import demo.services.BookService;
import net.minidev.json.JSONObject;
import org.hibernate.exception.JDBCConnectionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.InputMismatchException;

@RestController
public class AddBookCommand {
	private final BookService bookService;
	
	@Autowired
	public AddBookCommand(BookService bookService) {
		this.bookService = bookService;
	}
	
	@PostMapping(value = "/api/v1/books", consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<JSONObject> execute(@RequestBody @Valid BookDTO book) {
		JSONObject result = new JSONObject();
		BookDTO addedBook;
		try {
			if (bookService.getBookByName(book.getName()) != null) {
				addedBook = bookService.addBook(book);

				if (addedBook == null) {
					result.put("error", "Въвели сте книга с различаващи се имена и ISBN номера");
					return ResponseEntity.badRequest().body(result);
				}

				result.put("message", "Успешно променихте брой книги");
				return ResponseEntity.status(HttpStatus.CREATED).body(result);
			}

			bookService.addBook(book);
			result.put("message", "Успешно добавихте нова книга");
			return ResponseEntity.status(HttpStatus.CREATED).body(result);
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
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Невалиден номер на книга");
	}
}
