package demo.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.www.BasicAuthenticationEntryPoint;
import org.springframework.stereotype.Component;

@Component
public class BasicAuthEntryPoint extends BasicAuthenticationEntryPoint {

  @Override
  public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException ae) throws IOException {
    // setting the authentication scheme to Basic triggers a browser prompt for the credentials
    // that prompt is undeserved as we've got a custom login frontend page to take care of that
    // in this case, simply introduce a custom authentication scheme - BasicNoBrowserPrompt
    response.addHeader("WWW-Authenticate", "BasicNoBrowserPrompt realm=\"" + this.getRealmName() + "\"");
    // throwing ResponseStatusException here will create internal server error (after all, we handle the default unauthorized state here)
    // thus, we need to manually create an object, serialize it and send it
    this.createAndSendUnauthorizedResponse(response);
  }

  @Override
  public void afterPropertiesSet() {
    this.setRealmName("Library ILS");
    super.afterPropertiesSet();
  }


  private void createAndSendUnauthorizedResponse(HttpServletResponse httpServletResponse) throws IOException {
    httpServletResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
    Exception exceptionDto = new org.apache.tomcat.websocket.AuthenticationException(HttpStatus.UNAUTHORIZED.getReasonPhrase());
    String responseBody = new ObjectMapper().writeValueAsString(exceptionDto);
    this.sendResponse(httpServletResponse, responseBody);
  }

  private void sendResponse(HttpServletResponse httpServletResponse, String responseBody) throws IOException {
    PrintWriter out = httpServletResponse.getWriter();
    httpServletResponse.setContentType(MediaType.APPLICATION_JSON_VALUE);
    httpServletResponse.setCharacterEncoding("UTF-8");
    out.print(responseBody);
    out.flush();
  }
}
