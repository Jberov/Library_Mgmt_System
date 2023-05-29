package demo.command;

import demo.dto.UserDTO;
import demo.services.UserService;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UpdateUserCommand {
  private final UserService service;

  @Autowired
  public UpdateUserCommand(UserService service) {this.service = service;}


  @PutMapping(value = "/api/v1/users/{username}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.TEXT_PLAIN_VALUE)
  public ResponseEntity<String> updateUser(@PathVariable("username") String username ,@RequestBody @Valid UserDTO userDTO){
    service.updateUser(username, userDTO);
    return ResponseEntity.status(HttpStatus.CREATED).body("User successfully created");
  }

  @ExceptionHandler(IllegalArgumentException.class)
  @ResponseStatus(HttpStatus.I_AM_A_TEAPOT)
  @ResponseBody
  public ResponseEntity<String> userAlreadyExists(IllegalArgumentException ex) {
    return ResponseEntity.status(HttpStatus.I_AM_A_TEAPOT).body("No such user exists!");
  }
}
