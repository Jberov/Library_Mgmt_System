package demo.EventListeners;

import demo.dto.UserDTO;
import demo.mappers.UserMapper;
import demo.services.UserService;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.MessageSource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

@Component
public class RegistrationListener implements ApplicationListener<OnRegistrationCompleteEvent> {
  @Autowired
  private UserService service;

  @Autowired
  private MessageSource messages;

  @Autowired
  private JavaMailSender mailSender;

  @Autowired
  private UserMapper mapper;

  @Override
  public void onApplicationEvent(OnRegistrationCompleteEvent event) {
    this.confirmRegistration(event);
  }

  private void confirmRegistration(OnRegistrationCompleteEvent event) {
    UserDTO user = event.getUser();
    String token = UUID.randomUUID().toString();
    System.out.println(token);
    service.createVerificationToken(mapper.userDTOToEntity(user) , token);

    String recipientAddress = user.getEmail();
    System.out.println(recipientAddress);
    String subject = "Registration Confirmation";
    String confirmationUrl
        = event.getAppUrl() + "/regitrationConfirm?token=" + token;
    String message = messages.getMessage("message.regSucc", null, event.getLocale());
try{
  SimpleMailMessage email = new SimpleMailMessage();
  email.setTo(recipientAddress);
  email.setSubject(subject);
  email.setText(message + "\r\n" + "http://localhost:8080" + confirmationUrl);
  mailSender.send(email);
} catch (Exception e){
  System.out.println(e.getMessage());
}

  }
}
