package integrationTests;

import demo.command.AddBookCommand;
import demo.command.RemoveBookCommand;
import demo.dto.BookDTO;
import net.minidev.json.JSONObject;
import org.assertj.core.api.AssertionsForClassTypes;
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
        addBookCommand.execute(new BookDTO("9789542617303",6,"Марк Менсън","Тънкото изкуство да не ти пука","Desc",true));
    }
    
    @Test
    public void AddThenDeleteBook(){
        String url = "https://library-app.cfapps.sap.hana.ondemand.com/api/v1/books";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(accessToken);
        String json = "{\"isbn\":\"9780345342966\",\"author\":\"Ray Bradbury\",\"name\":\"Fahrenheit 451\",\"description\":\"Desc\",\"count\":3}";
        HttpEntity<String> request = new HttpEntity<>(json, headers);
        responseEntity = restTemplate.postForEntity(url, request, JSONObject.class);
        JSONObject response = responseEntity.getBody();
        AssertionsForClassTypes.assertThat(responseEntity.getStatusCode().is2xxSuccessful()).isTrue();
        AssertionsForClassTypes.assertThat(responseEntity.getBody()).isEqualTo(response);
        
        url = "https://library-app.cfapps.sap.hana.ondemand.com/api/v1/books";
        responseEntity = restTemplate.postForEntity(url, request, JSONObject.class);
        AssertionsForClassTypes.assertThat(responseEntity.getStatusCode().is2xxSuccessful()).isTrue();
        AssertionsForClassTypes.assertThat(Objects.requireNonNull(responseEntity.getBody()).toJSONString()).contains("\"count\":6");
    
        url = "https://library-app.cfapps.sap.hana.ondemand.com/api/v1/books/9780345342966";
        responseEntity = restTemplate.exchange(url, HttpMethod.DELETE, request, JSONObject.class);
        AssertionsForClassTypes.assertThat(responseEntity.getStatusCodeValue()).isEqualTo(200);
        AssertionsForClassTypes.assertThat(Objects.requireNonNull(responseEntity.getBody()).toJSONString()).contains("Book successfully deleted");
    }

    @Test
    public void getAllBooks() {
        String url = "https://library-app.cfapps.sap.hana.ondemand.com/api/v1/books";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(accessToken);
        HttpEntity<JSONObject> result = new HttpEntity<>(headers);
        responseEntity = restTemplate.exchange(url, HttpMethod.GET, result, JSONObject.class);
        JSONObject response = responseEntity.getBody();
        AssertionsForClassTypes.assertThat(responseEntity.getStatusCodeValue()).isEqualTo(200);
        AssertionsForClassTypes.assertThat(responseEntity.getBody()).isEqualTo(response);
    }

    @Test
    public void getBookTest() {
        String url = "https://library-app.cfapps.sap.hana.ondemand.com/api/v1/books/9789542617303";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(accessToken);
        HttpEntity<JSONObject> result = new HttpEntity<>(headers);
        responseEntity = restTemplate.exchange(url, HttpMethod.GET, result, JSONObject.class);
        AssertionsForClassTypes.assertThat(responseEntity.getStatusCodeValue()).isEqualTo(200);
        AssertionsForClassTypes.assertThat(Objects.requireNonNull(responseEntity.getBody()).toJSONString()).contains("Марк Менсън");
    }

    @Test
    public void getBookUsageTestNoBook() {
        String url = "https://library-app.cfapps.sap.hana.ondemand.com/api/v1/users/byBook/9798484500513";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(accessToken);
        HttpEntity<JSONObject> result = new HttpEntity<>(headers);
        responseEntity = restTemplate.exchange(url, HttpMethod.GET, result, JSONObject.class);
        AssertionsForClassTypes.assertThat(responseEntity.getStatusCodeValue()).isEqualTo(404);
        AssertionsForClassTypes.assertThat(Objects.requireNonNull(responseEntity.getBody()).toJSONString()).contains("No such book");
    }

    @Test
    public void getUserTest() {
        String url = "https://library-app.cfapps.sap.hana.ondemand.com/api/v1/users/info/yordan.berov@sap.com";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(accessToken);
        HttpEntity<JSONObject> result = new HttpEntity<>(headers);
        responseEntity = restTemplate.exchange(url, HttpMethod.GET, result, JSONObject.class);
        AssertionsForClassTypes.assertThat(responseEntity.getStatusCodeValue()).isEqualTo(200);
        AssertionsForClassTypes.assertThat(Objects.requireNonNull(responseEntity.getBody()).toJSONString()).contains("\"id\":0");
    }

    @Test
    public void getUserTestFail() {
        String url = "https://library-app.cfapps.sap.hana.ondemand.com/api/v1/users/info/asd";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(accessToken);
        HttpEntity<JSONObject> result = new HttpEntity<>(headers);
        responseEntity = restTemplate.exchange(url, HttpMethod.GET, result, JSONObject.class);
        AssertionsForClassTypes.assertThat(responseEntity.getStatusCodeValue()).isEqualTo(404);
        AssertionsForClassTypes.assertThat(Objects.requireNonNull(responseEntity.getBody()).toJSONString()).contains("No such user");
    }
    
    @Test
    public void leaseThenUsageThenDeleteNotReturnedBookThenReturnBookTestThenUsageWithReturnedBook (){
        String url = "https://library-app.cfapps.sap.hana.ondemand.com/api/v1/books";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(accessToken);
        String json = "{\"isbn\":\"9780345342966\",\"author\":\"Ray Bradbury\",\"name\":\"Fahrenheit 451\",\"description\":\"Desc\",\"count\":3}";
        HttpEntity<String> request = new HttpEntity<>(json, headers);
        responseEntity = restTemplate.postForEntity(url, request, JSONObject.class);
        
        url = "https://library-app.cfapps.sap.hana.ondemand.com/api/v1/books/rental/9780345342966";
        HttpEntity<JSONObject> result = new HttpEntity<>(headers);
        responseEntity = restTemplate.exchange(url, HttpMethod.PATCH, result, JSONObject.class);
        AssertionsForClassTypes.assertThat(responseEntity.getStatusCodeValue()).isEqualTo(200);
        AssertionsForClassTypes.assertThat(Objects.requireNonNull(responseEntity.getBody()).toJSONString()).contains("lease");
        
        url = "https://library-app.cfapps.sap.hana.ondemand.com/api/v1/users/byBook/9780345342966";
        responseEntity = restTemplate.exchange(url, HttpMethod.GET, result, JSONObject.class);
        AssertionsForClassTypes.assertThat(responseEntity.getStatusCodeValue()).isEqualTo(200);
        AssertionsForClassTypes.assertThat(Objects.requireNonNull(responseEntity.getBody()).toJSONString()).contains("users");
    
        url = "https://library-app.cfapps.sap.hana.ondemand.com/api/v1/books/9780345342966";
        responseEntity = restTemplate.exchange(url, HttpMethod.DELETE, result, JSONObject.class);
        AssertionsForClassTypes.assertThat(responseEntity.getStatusCodeValue()).isEqualTo(404);
        AssertionsForClassTypes.assertThat(Objects.requireNonNull(responseEntity.getBody()).toJSONString()).contains("not all users have returned");
    
        url = "https://library-app.cfapps.sap.hana.ondemand.com/api/v1/books/returns/9780345342966";
        responseEntity = restTemplate.exchange(url, HttpMethod.PATCH, result, JSONObject.class);
        AssertionsForClassTypes.assertThat(responseEntity.getStatusCodeValue()).isEqualTo(200);
        AssertionsForClassTypes.assertThat(Objects.requireNonNull(responseEntity.getBody()).toJSONString()).contains("returned");
    
        url = "https://library-app.cfapps.sap.hana.ondemand.com/api/v1/users/byBook/9780345342966";
        responseEntity = restTemplate.exchange(url, HttpMethod.GET, result, JSONObject.class);
        AssertionsForClassTypes.assertThat(responseEntity.getStatusCodeValue()).isEqualTo(404);
        AssertionsForClassTypes.assertThat(Objects.requireNonNull(responseEntity.getBody()).toJSONString()).contains("No users have taken this book");
        
        url = "https://library-app.cfapps.sap.hana.ondemand.com/api/v1/books/9780345342966";
        responseEntity = restTemplate.exchange(url, HttpMethod.DELETE, result, JSONObject.class);
        AssertionsForClassTypes.assertThat(responseEntity.getStatusCodeValue()).isEqualTo(200);
        AssertionsForClassTypes.assertThat(Objects.requireNonNull(responseEntity.getBody()).toJSONString()).contains("Book successfully deleted");
    }
    
    @Test
    public void leaseNonExistingBookTest() {
        String url = "https://library-app.cfapps.sap.hana.ondemand.com/api/v1/books/rental/9799542617303";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(accessToken);
        HttpEntity<JSONObject> result = new HttpEntity<>(headers);
        responseEntity = restTemplate.exchange(url, HttpMethod.PATCH, result, JSONObject.class);
        AssertionsForClassTypes.assertThat(responseEntity.getStatusCodeValue()).isEqualTo(404);
        AssertionsForClassTypes.assertThat(Objects.requireNonNull(responseEntity.getBody()).toJSONString()).contains("Book does not exist");
    }
    
    @Test
    public void userHistoryTest() {
        String url = "https://library-app.cfapps.sap.hana.ondemand.com/api/v1/users/history";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(accessToken);
        HttpEntity<JSONObject> result = new HttpEntity<>(headers);
        responseEntity = restTemplate.exchange(url, HttpMethod.GET, result, JSONObject.class);
        AssertionsForClassTypes.assertThat(responseEntity.getStatusCodeValue()).isEqualTo(200);
        AssertionsForClassTypes.assertThat(Objects.requireNonNull(responseEntity.getBody()).toJSONString()).contains("Currently taken books by user:");
    }

    @Test
    public void returnBookTestNoBook() {
        String url = "https://library-app.cfapps.sap.hana.ondemand.com/api/v1/books/returns/9799543617303";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(accessToken);
        HttpEntity<JSONObject> result = new HttpEntity<>(headers);
        responseEntity = restTemplate.exchange(url, HttpMethod.PATCH, result, JSONObject.class);
        AssertionsForClassTypes.assertThat(responseEntity.getStatusCodeValue()).isEqualTo(404);
        AssertionsForClassTypes.assertThat(Objects.requireNonNull(responseEntity.getBody()).toJSONString()).contains("No such book");
    }
    
    @AfterTestClass
    public void cleanup(){
        removeBookCommand.execute("9789543617303");
        removeBookCommand.execute("9780345342966");
    }
}
