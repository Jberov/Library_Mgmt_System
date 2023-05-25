package demo.command;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;

public class LoginCommand {

  @GetMapping(value = "/api/v1/login")
  public ResponseEntity<Void> execute(){
    return ResponseEntity.status(org.springframework.http.HttpStatus.OK).build();
  }
}
