package demo.command;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import demo.services.StatisticsService;
import java.time.LocalDate;
import java.util.Optional;
import net.minidev.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/statistics")
public class StatisticsController {
  private final ObjectMapper mapper = new ObjectMapper();

  private final StatisticsService service;

  @Autowired
  public StatisticsController(StatisticsService service) {
    this.service = service;
  }


  @GetMapping(value = "/mostPopularBooks",produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<JsonNode> getMostPopularByCriteria(@RequestBody(required = false) String date, @RequestHeader(value = "Criteria") String criteria){
    if (criteria.equals("genre")) {
      if (date != null) {
        return ResponseEntity.status(HttpStatus.OK).body(mapper.convertValue(service.getMostReadGenresByDate(Optional.of(LocalDate.parse(date))), JsonNode.class));
      }
      return ResponseEntity.status(HttpStatus.OK).body(mapper.convertValue(service.getMostReadGenresByDate(Optional.empty()), JsonNode.class));
    } else if (criteria.equals("author")) {
      if (date != null) {
        return ResponseEntity.status(HttpStatus.OK).body(mapper.convertValue(service.getMostReadAuthorsByDate(Optional.of(LocalDate.parse(date))), JsonNode.class));
      }
      return ResponseEntity.status(HttpStatus.OK).body(mapper.convertValue(service.getMostReadAuthorsByDate(Optional.empty()), JsonNode.class));
    }

    if (date != null) {
        return ResponseEntity.status(HttpStatus.OK).body(mapper.convertValue(service.getMostReadBooks(Optional.of(LocalDate.parse(date))), JsonNode.class));
    }

    return ResponseEntity.status(HttpStatus.OK).body(mapper.convertValue(service.getMostReadBooks(Optional.empty()), JsonNode.class));

  }

  @GetMapping(path = "/countOfBooks",consumes = MediaType.TEXT_PLAIN_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<JSONObject> getCountOfReadBooks(@RequestBody(required = false) String date) {
    JSONObject object = new JSONObject();
    if(date == null){
      object.put("countOfReadings", service.getCountOfReadBooks(Optional.empty()));
      return ResponseEntity.status(HttpStatus.OK).body(object);
    }
    object.put("countOfReadings", service.getCountOfReadBooks(Optional.of(LocalDate.parse(date))));
    return ResponseEntity.status(HttpStatus.OK).body(object);
  }

  @ExceptionHandler(ServletRequestBindingException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  @ResponseBody
  public ResponseEntity<String> headerMissing(ServletRequestBindingException ex) {
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("No criteria header set");
  }
}
