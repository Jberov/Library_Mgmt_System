package demo.EventListeners;

import java.util.Locale;
import org.springframework.context.ApplicationEvent;

public class PassChangeEvent extends ApplicationEvent {
  public PassChangeEvent(String user, Locale local) {
    super(user);
    this.locale = local;
    this.user = user;
  }

  public Locale getLocale() {
    return locale;
  }

  public void setLocale(Locale locale) {
    this.locale = locale;
  }

  private Locale locale;

  public String getUser() {
    return user;
  }

  public void setUser(String user) {
    this.user = user;
  }

  private String user;


}
