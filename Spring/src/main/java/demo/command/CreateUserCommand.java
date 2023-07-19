package demo.command;

import demo.EventListeners.OnRegistrationCompleteEvent;
import demo.dto.UserDTO;
import demo.entities.VerificationToken;
import demo.mappers.UserMapper;
import demo.services.UserService;
import java.util.Calendar;
import java.util.Locale;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;

@RestController
public class CreateUserCommand {

  private final UserService service;
  private final ApplicationEventPublisher eventPublisher;
  private final UserMapper mapper;
  private final MessageSource messages;

  @Autowired
  public CreateUserCommand(UserService service, ApplicationEventPublisher eventPublisher, UserMapper mapper, MessageSource messages) {
    this.service = service;
    this.eventPublisher = eventPublisher;
    this.mapper = mapper;
    this.messages = messages;
  }


  @PostMapping(value = "/api/v1/users", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.TEXT_PLAIN_VALUE)
  public ResponseEntity<String> createUser(@RequestBody @Valid UserDTO userDTO, HttpServletRequest request){
    if(service.userExistsByMail(userDTO.getEmail()) || service.userExistsByPhone(userDTO.getTelephoneNumber())){
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Email or telephone are already used");
    }
    UserDTO registered = service.createUser(userDTO);
    String appUrl = request.getContextPath();
    System.out.println("1");
    eventPublisher.publishEvent(new OnRegistrationCompleteEvent(registered,
        request.getLocale(), appUrl));
    System.out.println("1");

    return ResponseEntity.status(HttpStatus.CREATED).body("User successfully created");
  }

  @GetMapping("/registrationConfirm")
  public ResponseEntity<String> confirmRegistration
      (WebRequest request, Model model, @RequestParam("token") String token) {

    Locale locale = request.getLocale();

    VerificationToken verificationToken = service.getVerificationToken(token);
    if (verificationToken == null) {
      String message = "Wrong token";
      model.addAttribute("message", message);
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("redirect:/badUser.html?lang=" + locale.getLanguage());
    }

    UserDTO user = mapper.userToDTO(verificationToken.getUser());
    Calendar cal = Calendar.getInstance();
    if ((verificationToken.getExpiryDate().getTime() - cal.getTime().getTime()) <= 0) {
      String messageValue = "Expired token";
      model.addAttribute("message", messageValue);
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Expired token. User is not enabled");
    }

    service.enableUser(user.getUsername());
    return ResponseEntity.status(HttpStatus.OK).body("redirect:/login.html?lang=" + request.getLocale().getLanguage());
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  @ResponseBody
  public ResponseEntity<String> validationError(MethodArgumentNotValidException ex) {
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
  }

  @ExceptionHandler(IllegalArgumentException.class)
  @ResponseStatus(HttpStatus.CONFLICT)
  @ResponseBody
  public ResponseEntity<String> userAlreadyExists(IllegalArgumentException ex) {
    return ResponseEntity.status(HttpStatus.CONFLICT).body("Such user already exists!");
  }
}
