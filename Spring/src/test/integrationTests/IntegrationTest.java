package integrationTests;

import net.minidev.json.JSONObject;
import org.assertj.core.api.AssertionsForClassTypes;
import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;

import java.util.Objects;


public class IntegrationTest {
    private ResponseEntity<JSONObject> responseEntity;
    @Autowired
    private TestRestTemplate restTemplate;

    private String accessToken;


    @Test
    public void addBookTest(){
        String url = "https://library-app.cfapps.sap.hana.ondemand.com/api/v1/books";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(accessToken);
        String json = "{\"isbn\":\"9789542617303\",\"author\":\"Марк Менсън\",\"name\":\"Тънкото изкуство да не ти пука\",\"description\":\"Desc\",\"count\":3}";
        HttpEntity<String> request = new HttpEntity<>(json, headers);
        responseEntity = restTemplate.postForEntity( url, request , JSONObject.class );
        JSONObject response = responseEntity.getBody();
        AssertionsForClassTypes.assertThat(responseEntity.getStatusCode().is2xxSuccessful()).isTrue();
        AssertionsForClassTypes.assertThat(responseEntity.getBody()).isEqualTo(response);
    }
    @Test
    public void getAllBooks(){
        String url = "https://library-app.cfapps.sap.hana.ondemand.com/api/v1/books";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(accessToken);
        HttpEntity<JSONObject> result = new HttpEntity<>(headers);
        responseEntity = restTemplate.exchange(url,HttpMethod.GET,result,JSONObject.class);
        JSONObject response = responseEntity.getBody();
        AssertionsForClassTypes.assertThat(responseEntity.getStatusCodeValue()).isEqualTo(200);
        AssertionsForClassTypes.assertThat(responseEntity.getBody()).isEqualTo(response);
    }
    @Test
    public void getBookTest(){
        String url = "https://library-app.cfapps.sap.hana.ondemand.com/api/v1/books/9789542617303";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(accessToken);
        HttpEntity<JSONObject> result = new HttpEntity<>(headers);
        responseEntity = restTemplate.exchange(url,HttpMethod.GET,result,JSONObject.class);
        AssertionsForClassTypes.assertThat(responseEntity.getStatusCodeValue()).isEqualTo(200);
        AssertionsForClassTypes.assertThat(Objects.requireNonNull(responseEntity.getBody()).toJSONString()).contains("9789542617303");
    }
    @Test
    public void getBookUsageTestNoUsers(){
        String url = "https://library-app.cfapps.sap.hana.ondemand.com/api/v1/users/byBook/9788484500513";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(accessToken);
        HttpEntity<JSONObject> result = new HttpEntity<>(headers);
        responseEntity = restTemplate.exchange(url,HttpMethod.GET,result,JSONObject.class);
        AssertionsForClassTypes.assertThat(responseEntity.getStatusCodeValue()).isEqualTo(404);
        AssertionsForClassTypes.assertThat(Objects.requireNonNull(responseEntity.getBody()).toJSONString()).contains("No users have taken this book");
    }
    @Test
    public void getBookUsageTestNoBook(){
        String url = "https://library-app.cfapps.sap.hana.ondemand.com/api/v1/users/byBook/9798484500513";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(accessToken);
        HttpEntity<JSONObject> result = new HttpEntity<>(headers);
        responseEntity = restTemplate.exchange(url,HttpMethod.GET,result,JSONObject.class);
        AssertionsForClassTypes.assertThat(responseEntity.getStatusCodeValue()).isEqualTo(404);
        AssertionsForClassTypes.assertThat(Objects.requireNonNull(responseEntity.getBody()).toJSONString()).contains("No such book");
    }
    @Test
    public void getUserTest(){
        String url = "https://library-app.cfapps.sap.hana.ondemand.com/api/v1/users/info/yordan.berov@sap.com";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(accessToken);
        HttpEntity<JSONObject> result = new HttpEntity<>(headers);
        responseEntity = restTemplate.exchange(url,HttpMethod.GET,result,JSONObject.class);
        AssertionsForClassTypes.assertThat(responseEntity.getStatusCodeValue()).isEqualTo(200);
        AssertionsForClassTypes.assertThat(Objects.requireNonNull(responseEntity.getBody()).toJSONString()).contains("\"id\":0");
    }
    @Test
    public void getUserTestFail(){
        String url = "https://library-app.cfapps.sap.hana.ondemand.com/api/v1/users/info/asd";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(accessToken);
        HttpEntity<JSONObject> result = new HttpEntity<>(headers);
        responseEntity = restTemplate.exchange(url,HttpMethod.GET,result,JSONObject.class);
        AssertionsForClassTypes.assertThat(responseEntity.getStatusCodeValue()).isEqualTo(404);
        AssertionsForClassTypes.assertThat(Objects.requireNonNull(responseEntity.getBody()).toJSONString()).contains("No such user");
    }
    @Test
    public void leaseBookTest(){
        String url = "https://library-app.cfapps.sap.hana.ondemand.com/api/v1/books/rental/9789542617303";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(accessToken);
        HttpEntity<JSONObject> result = new HttpEntity<>(headers);
        responseEntity = restTemplate.exchange(url,HttpMethod.PATCH,result,JSONObject.class);
        AssertionsForClassTypes.assertThat(responseEntity.getStatusCodeValue()).isEqualTo(200);
        AssertionsForClassTypes.assertThat(Objects.requireNonNull(responseEntity.getBody()).toJSONString()).contains("9789542617303");
    }
    @Test
    public void leaseNonExistingBookTest(){
        String url = "https://library-app.cfapps.sap.hana.ondemand.com/api/v1/books/rental/9799542617303";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(accessToken);
        HttpEntity<JSONObject> result = new HttpEntity<>(headers);
        responseEntity = restTemplate.exchange(url,HttpMethod.PATCH,result,JSONObject.class);
        AssertionsForClassTypes.assertThat(responseEntity.getStatusCodeValue()).isEqualTo(200);
        AssertionsForClassTypes.assertThat(Objects.requireNonNull(responseEntity.getBody()).toJSONString()).contains("9789542617303");
    }
    @Test
    public void getBookUsageTest(){
        String url = "https://library-app.cfapps.sap.hana.ondemand.com/api/v1/users/byBook/9789542617303";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(accessToken);
        HttpEntity<JSONObject> result = new HttpEntity<>(headers);
        responseEntity = restTemplate.exchange(url,HttpMethod.GET,result,JSONObject.class);
        AssertionsForClassTypes.assertThat(responseEntity.getStatusCodeValue()).isEqualTo(200);
        AssertionsForClassTypes.assertThat(Objects.requireNonNull(responseEntity.getBody()).toJSONString()).contains("users");
    }
    @Test
    public void deleteNotReturnedBookTest(){
        String url = "https://library-app.cfapps.sap.hana.ondemand.com/api/v1/books/9789542617303";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(accessToken);
        HttpEntity<JSONObject> result = new HttpEntity<>(headers);
        responseEntity = restTemplate.exchange(url,HttpMethod.GET,result,JSONObject.class);
        AssertionsForClassTypes.assertThat(responseEntity.getStatusCodeValue()).isEqualTo(404);
        AssertionsForClassTypes.assertThat(Objects.requireNonNull(responseEntity.getBody()).toJSONString()).contains("Not all users have returned");
    }
    @Test
    public void returnBookTest(){
        String url = "https://library-app.cfapps.sap.hana.ondemand.com/api/v1/books/return/9789542617303";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(accessToken);
        HttpEntity<JSONObject> result = new HttpEntity<>(headers);
        responseEntity = restTemplate.exchange(url, HttpMethod.PATCH ,result,JSONObject.class);
        AssertionsForClassTypes.assertThat(responseEntity.getStatusCodeValue()).isEqualTo(200);
        AssertionsForClassTypes.assertThat(Objects.requireNonNull(responseEntity.getBody()).toJSONString()).contains("returned");
    }
    @Test
    public void userHistoryTest(){
        String url = "https://library-app.cfapps.sap.hana.ondemand.com/api/v1/users/history";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(accessToken);
        HttpEntity<JSONObject> result = new HttpEntity<>(headers);
        responseEntity = restTemplate.exchange(url,HttpMethod.GET,result,JSONObject.class);
        AssertionsForClassTypes.assertThat(responseEntity.getStatusCodeValue()).isEqualTo(200);
        AssertionsForClassTypes.assertThat(Objects.requireNonNull(responseEntity.getBody()).toJSONString()).contains("Currently taken books by user:");
    }
    @Test
    public void returnBookTestNoBook(){
        String url = "https://library-app.cfapps.sap.hana.ondemand.com/api/v1/books/return/9789543617303";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(accessToken);
        HttpEntity<JSONObject> result = new HttpEntity<>(headers);
        responseEntity = restTemplate.exchange(url,HttpMethod.PATCH,result,JSONObject.class);
        AssertionsForClassTypes.assertThat(responseEntity.getStatusCodeValue()).isEqualTo(404);
        AssertionsForClassTypes.assertThat(Objects.requireNonNull(responseEntity.getBody()).toJSONString()).contains("No such book");
    }
    @Test
    public void deleteBookTest(){
        String url = "https://library-app.cfapps.sap.hana.ondemand.com/api/v1/books/9789543617303";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(accessToken);
        HttpEntity<JSONObject> result = new HttpEntity<>(headers);
        responseEntity = restTemplate.exchange(url,HttpMethod.DELETE,result,JSONObject.class);
        AssertionsForClassTypes.assertThat(responseEntity.getStatusCodeValue()).isEqualTo(200);
        AssertionsForClassTypes.assertThat(Objects.requireNonNull(responseEntity.getBody()).toJSONString()).contains("\"exists\":false");
    }
}
