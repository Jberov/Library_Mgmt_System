package demo.command;

import demo.dto.UserDTO;
import demo.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
public class LoginCommand {

  private final UserService userService;

  @Autowired
  public LoginCommand(UserService userService) {
    this.userService = userService;
  }

  @GetMapping(value = "/login")
  public ResponseEntity<UserDTO> execute(Authentication au){
    return ResponseEntity.status(org.springframework.http.HttpStatus.OK).body(userService.getUser(au.getName()));
  }
}
