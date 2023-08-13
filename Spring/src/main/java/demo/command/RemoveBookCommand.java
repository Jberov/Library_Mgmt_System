package demo.command;

import demo.dto.BookDTO;
import demo.exceptions.BookLeaseException;
import demo.services.BookService;
import net.minidev.json.JSONObject;
import org.hibernate.exception.JDBCConnectionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RemoveBookCommand {
	
	private final BookService bookService;
	
	@Autowired
	public RemoveBookCommand(BookService bookService) {
		this.bookService = bookService;
	}
	
	@DeleteMapping(value = "api/v1/books/{name}")
	public ResponseEntity<JSONObject> execute(@PathVariable("name") String name) throws BookLeaseException {
		JSONObject result = new JSONObject();

			BookDTO deletedBook = bookService.deleteBook(name);
			if (deletedBook == null) {
				result.put("error", "Не съществува такава книга.");
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body(result);
			}
			
			result.put("Message", "Книгата " + deletedBook.getName() + " е успешно изтрита");
			return ResponseEntity.status(HttpStatus.OK).body(result);
	}

	@ExceptionHandler(JDBCConnectionException.class)
	@ResponseStatus(HttpStatus.GATEWAY_TIMEOUT)
	@ResponseBody
	public ResponseEntity<JSONObject> validationError(JDBCConnectionException ex) {
		JSONObject result = new JSONObject();
		result.put("error", "Грешка в системата");
		return ResponseEntity.status(HttpStatus.GATEWAY_TIMEOUT).body(result);
	}

	@ExceptionHandler(Exception.class)
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	@ResponseBody
	public ResponseEntity<JSONObject> validationError(Exception ex) {
		JSONObject result = new JSONObject();
		result.put("error", "Системата е временно недостъпна");
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(result);
	}

	@ExceptionHandler(BookLeaseException.class)
	@ResponseStatus(HttpStatus.CONFLICT)
	@ResponseBody
	public ResponseEntity<JSONObject> validationError(BookLeaseException ex) {
		JSONObject result = new JSONObject();
		result.put("error", "Книгата все още е в употреба");
		return ResponseEntity.status(HttpStatus.CONFLICT).body(result);
	}
}
