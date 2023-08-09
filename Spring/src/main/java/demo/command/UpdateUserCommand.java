package demo.command;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import demo.dto.UserDTO;
import demo.services.UserService;
import javax.validation.Valid;
import net.minidev.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/users")
public class UpdateUserCommand {
  private final UserService service;

  @Autowired
  public UpdateUserCommand(UserService service) {this.service = service;}


  @PutMapping(value = "/{username}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<String> updateUser(@PathVariable(value = "username", required = false) String username ,@RequestBody @Valid UserDTO userDTO, Authentication authentication){
    ObjectMapper mapper = new ObjectMapper();
    JSONObject result = new JSONObject();
    JsonNode node;
    if (authentication.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ADMIN") && username != null)) {
      node = mapper.convertValue(service.updateUser(username, userDTO), JsonNode.class);
    } else {
      UserDTO persistedUser = service.getUser(username);

      if (!userDTO.getRole().equals(persistedUser.getRole()) && authentication.getAuthorities().stream().noneMatch(a -> a.getAuthority().equals("ADMIN")))
      {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Потребителят не може да си смени ролята");
      }

      node = mapper.convertValue(service.updateUser(authentication.getName(), userDTO), JsonNode.class);
    }
    result.put("message", "User successfully updated");
    result.put("user", node);
    return ResponseEntity.status(HttpStatus.OK).body("User successfully updated\n" + result);
  }

  @ExceptionHandler(NullPointerException.class)
  @ResponseStatus(HttpStatus.NOT_FOUND)
  @ResponseBody
  public ResponseEntity<String> userAlreadyExists(IllegalArgumentException ex) {
    return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No such user exists!");
  }
}
