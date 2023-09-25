package demo.command;

import demo.EventListeners.OnRegistrationCompleteEvent;
import demo.EventListeners.PassChangeEvent;
import demo.dto.UserDTO;
import demo.services.UserService;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
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
  private final ApplicationEventPublisher eventPublisher;


  @Autowired
  public UpdateUserCommand(UserService service, ApplicationEventPublisher eventPublisher) {this.service = service; this.eventPublisher = eventPublisher;}

  @PostMapping(value = "/{username}/resetPassword", produces = MediaType.TEXT_PLAIN_VALUE)
  public ResponseEntity<String> sendMailForPass(@PathVariable(value = "username") String username, HttpServletRequest request){
    eventPublisher.publishEvent(new PassChangeEvent(username, request.getLocale()));
    return ResponseEntity.status(HttpStatus.OK).body("Успешно изпратена парола");
  }


  @PutMapping(value = "/{username}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<String> updateUser(@PathVariable(value = "username", required = false) String username ,@RequestBody @Valid UserDTO userDTO, Authentication authentication, HttpServletRequest request){
    userDTO.setEnabled(true);
    if (authentication.getAuthorities().stream().noneMatch(a -> a.getAuthority().equals("ADMIN") && username != null)) {
      UserDTO persistedUser = service.getUser(username);
      if (!userDTO.getRole().equals(persistedUser.getRole()) && authentication.getAuthorities().stream().noneMatch(a -> a.getAuthority().equals("ADMIN")))
      {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Потребителят не може да си смени ролята");
      }
    }

    if (!service.checkForPasswordChange(username, userDTO)){
      String appUrl = request.getContextPath();
      eventPublisher.publishEvent(new OnRegistrationCompleteEvent(userDTO,
          request.getLocale(), appUrl));
    }

    service.updateUser(username, userDTO);
    return ResponseEntity.status(HttpStatus.OK).body("Успешно обновен потребител");
  }

  @ExceptionHandler(NullPointerException.class)
  @ResponseStatus(HttpStatus.NOT_FOUND)
  @ResponseBody
  public ResponseEntity<String> userAlreadyExists(IllegalArgumentException ex) {
    return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Няма такъв потребител");
  }
}
