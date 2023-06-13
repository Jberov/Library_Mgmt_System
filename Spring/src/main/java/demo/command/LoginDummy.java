package demo.command;

import net.minidev.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LoginDummy {

  @PostMapping(value = "/login")
  public ResponseEntity<JSONObject> execute(Authentication authentication) {
    JSONObject result = new JSONObject();
    result.put("Role", authentication.getAuthorities());

    return ResponseEntity.status(HttpStatus.OK).body(result);
  }
}
