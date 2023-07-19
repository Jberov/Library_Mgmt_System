package demo.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class MailService {

  @Value("spring.mail.username")
  private String sender;

  @Autowired
  public MailService(JavaMailSender mailSender) {
    this.mailSender = mailSender;
  }

  private final JavaMailSender mailSender;


  public void sendMail(String recipient, String subject, String message){
    SimpleMailMessage messageMail = new SimpleMailMessage();
    messageMail.setFrom("SmartLibraryService <" + sender + ">");
    messageMail.setTo(recipient);
    messageMail.setSubject(subject);
    messageMail.setText(message);
    mailSender.send(messageMail);
  }
}
