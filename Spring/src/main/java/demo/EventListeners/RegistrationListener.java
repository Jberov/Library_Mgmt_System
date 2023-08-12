package demo.EventListeners;

import demo.dto.UserDTO;
import demo.mappers.UserMapper;
import demo.services.MailService;
import demo.services.UserService;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Component
public class RegistrationListener implements ApplicationListener<OnRegistrationCompleteEvent> {
  private final UserService service;

  @Autowired
  public RegistrationListener(UserService service, MailService mailService, UserMapper mapper) {
    this.service = service;
    this.mailService = mailService;
    this.mapper = mapper;
  }

  private final MailService mailService;

  private final UserMapper mapper;

  @Override
  public void onApplicationEvent(OnRegistrationCompleteEvent event) {
    this.confirmRegistration(event);
  }

  private void confirmRegistration(OnRegistrationCompleteEvent event) {
    UserDTO user = event.getUser();
    String token = UUID.randomUUID().toString();
    service.createVerificationToken(mapper.userDTOToEntity(user) , token);

    String recipientAddress = user.getEmail();
    String subject = "Registration Confirmation";
    String message = "You registered successfully. We will send you a confirmation message to your email account." + "\r\n" + "http://localhost/bootstrap-5-login-cover-template-main/index.html?token=" + token;
    mailService.sendMail(recipientAddress, subject, message);
  }
}
