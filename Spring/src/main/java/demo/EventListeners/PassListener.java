package demo.EventListeners;

import demo.services.MailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Component
public class PassListener implements ApplicationListener<PassChangeEvent> {

  @Autowired
  public PassListener(MailService mailService) {
    this.mailService = mailService;
  }

  private final MailService mailService;

  @Override
  public void onApplicationEvent(PassChangeEvent event) {
    this.resendPassMail(event.getUser());
  }

  private void resendPassMail(String user) {
    String recipientAddress = "jballer878@gmail.com";
    String subject = "Pass change request";
    String message = "A request was sent to change password from " + user + " to change password";
    mailService.sendMail(recipientAddress, subject, message);
  }
}
