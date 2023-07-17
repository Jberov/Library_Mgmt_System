package demo.EventListeners;

import demo.dto.UserDTO;
import java.util.Locale;
import org.springframework.context.ApplicationEvent;

public class OnRegistrationCompleteEvent extends ApplicationEvent {
  public String getAppUrl() {
    return appUrl;
  }

  public void setAppUrl(String appUrl) {
    this.appUrl = appUrl;
  }

  public Locale getLocale() {
    return locale;
  }

  public void setLocale(Locale locale) {
    this.locale = locale;
  }

  public UserDTO getUser() {
    return user;
  }

  public void setUser(UserDTO user) {
    this.user = user;
  }

  private String appUrl;
  private Locale locale;
  private UserDTO user;

  public OnRegistrationCompleteEvent(
      UserDTO user, Locale locale, String appUrl) {
    super(user);

    this.user = user;
    this.locale = locale;
    this.appUrl = appUrl;
  }
}
