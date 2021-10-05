package integrationTest;

import net.minidev.json.JSONObject;
import org.assertj.core.api.AssertionsForClassTypes;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.test.context.event.annotation.BeforeTestClass;

import java.util.Objects;

public class IntegrationTest {
	private static final String accessToken = "";
	private static final String json = "{\"isbn\":\"9780345342966\",\"author\":\"Ray Bradbury\",\"name\":\"Fahrenheit 451\",\"description\":\"Desc\",\"count\":3}";
	private static final String url = "https://library-app.cfapps.sap.hana.ondemand.com/api/v1/books";
	private static final String urlWithBook = url + "/9780345342966";
	private static final String urlUserInfo = "https://library-app.cfapps.sap.hana.ondemand.com/api/v1/users/info/";
	private static final String urlUsersByBook = "https://library-app.cfapps.sap.hana.ondemand.com/api/v1/users/byBook/9780345342966";
	private static final String username = "";
	private static final String deleteUserUrl = "https://library-app.cfapps.sap.hana.ondemand.com/api/v1/users/" + username;

	private final TestRestTemplate restTemplate = new TestRestTemplate();
	private ResponseEntity<JSONObject> responseEntity;
	
	public IntegrationTest() {
	}
	
	@BeforeTestClass
	public void setup() {
		restTemplate.getRestTemplate().setRequestFactory(new HttpComponentsClientHttpRequestFactory());
	}
	
	@Test
	public void completeCRUDBookTest() {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.setBearerAuth(accessToken);
		
		HttpEntity<String> request = new HttpEntity<>(json, headers);
		responseEntity = restTemplate.postForEntity(url, request, JSONObject.class);
		
		HttpEntity<JSONObject> result = new HttpEntity<>(headers);
		responseEntity = restTemplate.exchange(urlWithBook, HttpMethod.GET, result, JSONObject.class);
		
		Assertions.assertEquals(200, responseEntity.getStatusCodeValue());
		Assertions.assertTrue(Objects.requireNonNull(responseEntity.getBody()).toJSONString().contains("Ray Bradbury"));
		
		responseEntity = restTemplate.postForEntity(url, request, JSONObject.class);
		
		Assertions.assertEquals(responseEntity.getStatusCode().value(), 201);
		Assertions.assertTrue(Objects.requireNonNull(responseEntity.getBody()).toJSONString().contains("\"count\":6"));
		
		responseEntity = restTemplate.exchange(url, HttpMethod.GET, result, JSONObject.class);
		JSONObject response = responseEntity.getBody();
		
		Assertions.assertEquals(200, responseEntity.getStatusCodeValue());
		Assertions.assertEquals(responseEntity.getBody(), response);
		
		responseEntity = restTemplate.exchange(urlWithBook, HttpMethod.DELETE, request, JSONObject.class);
		Assertions.assertEquals(responseEntity.getStatusCodeValue(), 200);
		Assertions.assertTrue(Objects.requireNonNull(responseEntity.getBody()).toJSONString().contains("9780345342966"));
	}
	
	@Test
	public void getBookUsageTestNoBook() {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.setBearerAuth(accessToken);
		
		String url = "https://library-app.cfapps.sap.hana.ondemand.com/api/v1/users/byBook/9798484500513";
		HttpEntity<JSONObject> result = new HttpEntity<>(headers);
		responseEntity = restTemplate.exchange(url, HttpMethod.GET, result, JSONObject.class);
		
		Assertions.assertEquals(responseEntity.getStatusCodeValue(), 404);
		Assertions.assertTrue(Objects.requireNonNull(responseEntity.getBody()).toJSONString().contains("No such book"));
	}
	
	@Test
	public void getUserFailTest() {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.setBearerAuth(accessToken);
		
		HttpEntity<JSONObject> result = new HttpEntity<>(headers);
		responseEntity = restTemplate.exchange(urlUserInfo + "asd", HttpMethod.GET, result, JSONObject.class);
		
		Assertions.assertEquals(responseEntity.getStatusCodeValue(), 404);
		Assertions.assertTrue(Objects.requireNonNull(responseEntity.getBody()).toJSONString().contains("No such user"));
	}
	
	@Test
	public void testEnd2End() {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.setBearerAuth(accessToken);
		
		HttpEntity<String> request = new HttpEntity<>(json, headers);
		responseEntity = restTemplate.postForEntity(url, request, JSONObject.class);
		
		HttpEntity<JSONObject> result = new HttpEntity<>(headers);
		responseEntity = restTemplate.exchange(url + "/rental/9780345342966", HttpMethod.PATCH, result, JSONObject.class);
		
		Assertions.assertEquals(200, responseEntity.getStatusCodeValue());
		Assertions.assertTrue(Objects.requireNonNull(Objects.requireNonNull(responseEntity.getBody()).toJSONString()).contains("lease"));
  
		result = new HttpEntity<>(headers);
        responseEntity = restTemplate.exchange(urlUserInfo + "yordan.berov@sap.com", HttpMethod.GET, result, JSONObject.class);
        
        Assertions.assertEquals(200, responseEntity.getStatusCodeValue());
        Assertions.assertTrue(Objects.requireNonNull(responseEntity.getBody()).toJSONString().contains("\"id\":0"));
        Assertions.assertTrue(Objects.requireNonNull(responseEntity.getBody()).toJSONString().contains("\"Already returned books by user:\""));
		
		responseEntity = restTemplate.exchange(urlUsersByBook, HttpMethod.GET, result, JSONObject.class);
		
		Assertions.assertEquals(200, responseEntity.getStatusCodeValue());
		Assertions.assertTrue(Objects.requireNonNull(Objects.requireNonNull(responseEntity.getBody()).toJSONString()).contains("users"));
		
		responseEntity = restTemplate.exchange(urlWithBook, HttpMethod.DELETE, result, JSONObject.class);
		
		Assertions.assertEquals(404, responseEntity.getStatusCodeValue());
		Assertions.assertTrue(Objects.requireNonNull(Objects.requireNonNull(responseEntity.getBody()).toJSONString()).contains("not all users have returned"));
		
		responseEntity = restTemplate.exchange(url + "/returns/9780345342966", HttpMethod.PATCH, result, JSONObject.class);
		
		Assertions.assertEquals(200, responseEntity.getStatusCodeValue());
		Assertions.assertTrue(Objects.requireNonNull(Objects.requireNonNull(responseEntity.getBody()).toJSONString()).contains("returned"));
		
		responseEntity = restTemplate.exchange(urlUsersByBook, HttpMethod.GET, result, JSONObject.class);
		
		Assertions.assertEquals(404, responseEntity.getStatusCodeValue());
		Assertions.assertTrue(Objects.requireNonNull(Objects.requireNonNull(responseEntity.getBody()).toJSONString()).contains("No users have taken this book"));
		
		responseEntity = restTemplate.exchange(urlWithBook, HttpMethod.DELETE, result, JSONObject.class);
		
		Assertions.assertEquals(200, responseEntity.getStatusCodeValue());
		Assertions.assertTrue(Objects.requireNonNull(responseEntity.getBody()).toJSONString().contains("Book successfully deleted"));
        
        responseEntity = restTemplate.exchange(deleteUserUrl, HttpMethod.DELETE, result, JSONObject.class);
        
        Assertions.assertEquals(200, responseEntity.getStatusCodeValue());
        Assertions.assertTrue(Objects.requireNonNull(responseEntity.getBody()).toJSONString().contains("User successfully deleted"));
	}
	
	@Test
	public void leaseNonExistingBookTest() {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.setBearerAuth(accessToken);
		
		String localUrl = url + "/rental/9799542617303";
		HttpEntity<JSONObject> result = new HttpEntity<>(headers);
		responseEntity = restTemplate.exchange(localUrl, HttpMethod.PATCH, result, JSONObject.class);
		
		Assertions.assertEquals(404, responseEntity.getStatusCodeValue());
		AssertionsForClassTypes.assertThat(Objects.requireNonNull(responseEntity.getBody()).toJSONString()).contains("Book does not exist");
	}
	
	@Test
	public void returnBookTestNoBook() {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.setBearerAuth(accessToken);
		
		String localUrl = url + "/returns/9799543617303";
		HttpEntity<JSONObject> result = new HttpEntity<>(headers);
		responseEntity = restTemplate.exchange(localUrl, HttpMethod.PATCH, result, JSONObject.class);
		
		Assertions.assertEquals(responseEntity.getStatusCodeValue(), 404);
		AssertionsForClassTypes.assertThat(Objects.requireNonNull(responseEntity.getBody()).toJSONString()).contains("No such book");
	}
}
