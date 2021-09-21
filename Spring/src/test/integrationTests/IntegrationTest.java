package integrationTests;

import demo.command.AddBookCommand;
import demo.command.RemoveBookCommand;
import demo.dto.BookDTO;
import net.minidev.json.JSONObject;
import org.assertj.core.api.AssertionsForClassTypes;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.test.context.event.annotation.AfterTestClass;
import org.springframework.test.context.event.annotation.BeforeTestClass;
import java.util.Objects;

public class IntegrationTest {
    private final TestRestTemplate restTemplate = new TestRestTemplate();
    private final String accessToken = "";
    private ResponseEntity<JSONObject> responseEntity;
    private AddBookCommand addBookCommand;
    private RemoveBookCommand removeBookCommand;
    
    public IntegrationTest() {
    }
    
    @BeforeTestClass
    public void setup(){
        restTemplate.getRestTemplate().setRequestFactory(new HttpComponentsClientHttpRequestFactory());
        addBookCommand.execute(new BookDTO("9789542617303",6,"Марк Менсън","Тънкото изкуство да не ти пука","Desc"));
    }
    
    @Test
    public void AddThenDeleteBook(){
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(accessToken);
        
        String url = "https://library-app.cfapps.sap.hana.ondemand.com/api/v1/books";
        String json = "{\"isbn\":\"9780345342966\",\"author\":\"Ray Bradbury\",\"name\":\"Fahrenheit 451\",\"description\":\"Desc\",\"count\":3}";
        HttpEntity<String> request = new HttpEntity<>(json, headers);
        responseEntity = restTemplate.postForEntity(url, request, JSONObject.class);
        JSONObject response = responseEntity.getBody();
        
        Assertions.assertEquals(responseEntity.getStatusCode().value(), 201);
        Assertions.assertEquals(responseEntity.getBody(),response);
        
        url = "https://library-app.cfapps.sap.hana.ondemand.com/api/v1/books";
        responseEntity = restTemplate.postForEntity(url, request, JSONObject.class);
        
        Assertions.assertEquals(responseEntity.getStatusCode().value(), 201);
        Assertions.assertTrue(Objects.requireNonNull(responseEntity.getBody()).toJSONString().contains("\"count\":6"));
    
        url = "https://library-app.cfapps.sap.hana.ondemand.com/api/v1/books/9780345342966";
        responseEntity = restTemplate.exchange(url, HttpMethod.DELETE, request, JSONObject.class);
        
        Assertions.assertEquals(responseEntity.getStatusCodeValue(), 200);
        Assertions.assertTrue(Objects.requireNonNull(responseEntity.getBody()).toJSONString().contains("Book successfully deleted"));
    }

    @Test
    public void getAllBooks() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(accessToken);
        
        String url = "https://library-app.cfapps.sap.hana.ondemand.com/api/v1/books";
        HttpEntity<JSONObject> result = new HttpEntity<>(headers);
        responseEntity = restTemplate.exchange(url, HttpMethod.GET, result, JSONObject.class);
        JSONObject response = responseEntity.getBody();
        
        Assertions.assertEquals(responseEntity.getStatusCodeValue(),200);
        Assertions.assertEquals(responseEntity.getBody(), response);
    }

    @Test
    public void getBookTest() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(accessToken);
        
        String url = "https://library-app.cfapps.sap.hana.ondemand.com/api/v1/books/9789542617303";
        HttpEntity<JSONObject> result = new HttpEntity<>(headers);
        responseEntity = restTemplate.exchange(url, HttpMethod.GET, result, JSONObject.class);
        
        Assertions.assertEquals(responseEntity.getStatusCodeValue(), 200);
        Assertions.assertTrue(Objects.requireNonNull(responseEntity.getBody()).toJSONString().contains("Марк Менсън"));
    }

    @Test
    public void getBookUsageTestNoBook() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(accessToken);
        
        String url = "https://library-app.cfapps.sap.hana.ondemand.com/api/v1/users/byBook/9798484500513";
        HttpEntity<JSONObject> result = new HttpEntity<>(headers);
        responseEntity = restTemplate.exchange(url, HttpMethod.GET, result, JSONObject.class);
        
        Assertions.assertEquals(responseEntity.getStatusCodeValue(),404);
        Assertions.assertTrue(Objects.requireNonNull(responseEntity.getBody()).toJSONString().contains("No such book"));
    }

    @Test
    public void getUserTest() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(accessToken);
        
        String url = "https://library-app.cfapps.sap.hana.ondemand.com/api/v1/users/info/yordan.berov@sap.com";
        HttpEntity<JSONObject> result = new HttpEntity<>(headers);
        responseEntity = restTemplate.exchange(url, HttpMethod.GET, result, JSONObject.class);
        
        Assertions.assertEquals(responseEntity.getStatusCodeValue(), 200);
        Assertions.assertTrue(Objects.requireNonNull(responseEntity.getBody()).toJSONString().contains("\"id\":0"));
    }

    @Test
    public void getUserTestFail() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(accessToken);
        
        String url = "https://library-app.cfapps.sap.hana.ondemand.com/api/v1/users/info/asd";
        HttpEntity<JSONObject> result = new HttpEntity<>(headers);
        responseEntity = restTemplate.exchange(url, HttpMethod.GET, result, JSONObject.class);
        
        Assertions.assertEquals(responseEntity.getStatusCodeValue(), 404);
        Assertions.assertTrue(Objects.requireNonNull(responseEntity.getBody()).toJSONString().contains("No such user"));
    }
    
    @Test
    public void leaseThenUsageThenDeleteNotReturnedBookThenReturnBookTestThenUsageWithReturnedBook (){
        
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(accessToken);
        
        String url = "https://library-app.cfapps.sap.hana.ondemand.com/api/v1/books";
        String json = "{\"isbn\":\"9780345342966\",\"author\":\"Ray Bradbury\",\"name\":\"Fahrenheit 451\",\"description\":\"Desc\",\"count\":3}";
        HttpEntity<String> request = new HttpEntity<>(json, headers);
        responseEntity = restTemplate.postForEntity(url, request, JSONObject.class);
        
        url = "https://library-app.cfapps.sap.hana.ondemand.com/api/v1/books/rental/9780345342966";
        HttpEntity<JSONObject> result = new HttpEntity<>(headers);
        responseEntity = restTemplate.exchange(url, HttpMethod.PATCH, result, JSONObject.class);
        
        Assertions.assertEquals(responseEntity.getStatusCodeValue(), 200);
        Assertions.assertTrue(Objects.requireNonNull(Objects.requireNonNull(responseEntity.getBody()).toJSONString()).contains("lease"));
        
        url = "https://library-app.cfapps.sap.hana.ondemand.com/api/v1/users/byBook/9780345342966";
        responseEntity = restTemplate.exchange(url, HttpMethod.GET, result, JSONObject.class);
        
        Assertions.assertEquals(responseEntity.getStatusCodeValue(), 200);
        Assertions.assertTrue(Objects.requireNonNull(Objects.requireNonNull(responseEntity.getBody()).toJSONString()).contains("users"));
    
        url = "https://library-app.cfapps.sap.hana.ondemand.com/api/v1/books/9780345342966";
        responseEntity = restTemplate.exchange(url, HttpMethod.DELETE, result, JSONObject.class);
        
        Assertions.assertEquals(responseEntity.getStatusCodeValue(), 200);
        Assertions.assertTrue(Objects.requireNonNull(Objects.requireNonNull(responseEntity.getBody()).toJSONString()).contains("not all users have returned"));
    
        url = "https://library-app.cfapps.sap.hana.ondemand.com/api/v1/books/returns/9780345342966";
        responseEntity = restTemplate.exchange(url, HttpMethod.PATCH, result, JSONObject.class);
        
        Assertions.assertEquals(responseEntity.getStatusCodeValue(), 200);
        Assertions.assertTrue(Objects.requireNonNull(Objects.requireNonNull(responseEntity.getBody()).toJSONString()).contains("returned"));
    
        url = "https://library-app.cfapps.sap.hana.ondemand.com/api/v1/users/byBook/9780345342966";
        responseEntity = restTemplate.exchange(url, HttpMethod.GET, result, JSONObject.class);
        
        Assertions.assertEquals(responseEntity.getStatusCodeValue(), 200);
        Assertions.assertTrue(Objects.requireNonNull(Objects.requireNonNull(responseEntity.getBody()).toJSONString()).contains("No users have taken this book"));
        
        url = "https://library-app.cfapps.sap.hana.ondemand.com/api/v1/books/9780345342966";
        responseEntity = restTemplate.exchange(url, HttpMethod.DELETE, result, JSONObject.class);
        
        Assertions.assertEquals(responseEntity.getStatusCodeValue(), 200);
        Assertions.assertTrue(Objects.requireNonNull(responseEntity.getBody()).toJSONString().contains("Book successfully deleted"));
    }
    
    @Test
    public void leaseNonExistingBookTest() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(accessToken);
        
        String url = "https://library-app.cfapps.sap.hana.ondemand.com/api/v1/books/rental/9799542617303";
        HttpEntity<JSONObject> result = new HttpEntity<>(headers);
        responseEntity = restTemplate.exchange(url, HttpMethod.PATCH, result, JSONObject.class);
        
        Assertions.assertEquals(responseEntity.getStatusCodeValue(), 404);
        AssertionsForClassTypes.assertThat(Objects.requireNonNull(responseEntity.getBody()).toJSONString()).contains("Book does not exist");
    }
    
    @Test
    public void userHistoryTest() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(accessToken);
        
        String url = "https://library-app.cfapps.sap.hana.ondemand.com/api/v1/users/history";
        HttpEntity<JSONObject> result = new HttpEntity<>(headers);
        responseEntity = restTemplate.exchange(url, HttpMethod.GET, result, JSONObject.class);
        
        Assertions.assertEquals(responseEntity.getStatusCodeValue(), 200);
        AssertionsForClassTypes.assertThat(Objects.requireNonNull(responseEntity.getBody()).toJSONString()).contains("Currently taken books by user:");
    }

    @Test
    public void returnBookTestNoBook() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(accessToken);
        
        String url = "https://library-app.cfapps.sap.hana.ondemand.com/api/v1/books/returns/9799543617303";
        HttpEntity<JSONObject> result = new HttpEntity<>(headers);
        responseEntity = restTemplate.exchange(url, HttpMethod.PATCH, result, JSONObject.class);
        
        Assertions.assertEquals(responseEntity.getStatusCodeValue(), 404);
        AssertionsForClassTypes.assertThat(Objects.requireNonNull(responseEntity.getBody()).toJSONString()).contains("No such book");
    }
    
    @AfterTestClass
    public void cleanup(){
        removeBookCommand.execute("9789543617303");
        removeBookCommand.execute("9780345342966");
    }
}
