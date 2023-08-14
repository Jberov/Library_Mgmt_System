package demo.services;

import demo.dao.BookRecordsDAO;
import demo.entities.BooksActivity;
import demo.entities.User;
import demo.enums.Status;
import java.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class AutoReminderService {
  private final BookRecordsDAO recordsDAO;

  @Autowired
  public AutoReminderService(BookRecordsDAO recordsDAO, MailService mailService) {
    this.recordsDAO = recordsDAO;
    this.mailService = mailService;
  }

  private final MailService mailService;

  @Scheduled(cron = "0 0 */24 * * *")
  protected void runChecksForReturns(){
    for(BooksActivity activity : recordsDAO.getAllActivity()) {
      User user = activity.getUser();
      if (activity.getStatus().equals(Status.TAKEN) && activity.getReturnDate().isBefore(LocalDate.now())){
          mailService.sendMail(user.getEmail(),"Напомяне за книга", "Здравейте,\nИскаме да Ви напомним, че сте превишили "
              + "срока си за връщане на книга " + activity.getBook().getName() + "\nПоздрави, \n Библиотеката");
      }
    }
  }
}
