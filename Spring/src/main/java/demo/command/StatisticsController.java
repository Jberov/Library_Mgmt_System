package demo.command;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import demo.services.StatisticsService;
import java.time.LocalDate;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
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


  @GetMapping(path = "/mostPopularBooks",produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<JsonNode> getMostPopularByCriteria(@RequestBody(required = false) JsonNode date, @RequestHeader("Criteria") String criteria){
    if (criteria.equals("genre")) {
      if (!date.isNull() || !date.isEmpty()) {
        return ResponseEntity.status(HttpStatus.OK).body(mapper.convertValue(service.getMostReadGenresByDate(Optional.of(LocalDate.parse(date.get("date").toString()))), JsonNode.class));
      } else {
        return ResponseEntity.status(HttpStatus.OK).body(mapper.convertValue(service.getMostReadBooks(Optional.empty()), JsonNode.class));
      }
    } else {
      if (!date.isNull() || !date.isEmpty()) {
        return ResponseEntity.status(HttpStatus.OK).body(mapper.convertValue(service.getMostReadBooks(Optional.of(LocalDate.parse(date.get("date").toString()))), JsonNode.class));
      } else {
        return ResponseEntity.status(HttpStatus.OK).body(mapper.convertValue(service.getMostReadBooks(Optional.empty()), JsonNode.class));
      }
    }
  }

  @GetMapping(path = "/countOfBooks",produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<JsonNode> getCountOfReadBooks(@RequestBody(required = false) JsonNode date) {
    System.out.println(LocalDate.now());
    if(date == null){
      return ResponseEntity.status(HttpStatus.OK).body(mapper.convertValue(service.getCountOfReadBooks(Optional.empty()), JsonNode.class));
    }
    return ResponseEntity.status(HttpStatus.OK).body(mapper.convertValue(service.getCountOfReadBooks(Optional.of(LocalDate.parse(date.get("date").toString()))), JsonNode.class));
  }
}
