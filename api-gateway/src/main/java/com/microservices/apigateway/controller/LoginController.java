package com.microservices.apigateway.controller;

import com.microservices.apigateway.object.UserLogin;
import com.microservices.apigateway.object.UserRegistration;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;


@RestController
@RequestMapping("/user")
@PropertySource("classpath:application.properties")
public class LoginController {

    private final RestTemplate restTemplate;

    @Autowired
    public LoginController(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Value("${keycloak.admin-cli.spring-boot-microservices-realm.client-secret}")
    private String clientSecret;

    @GetMapping("/register-user")
    public ResponseEntity<String> registerUser(@RequestBody UserRegistration userRegistration) throws JSONException {
        String adminCliToken = getAdminCliToken();

        String url = "/admin/realms/spring-boot-microservices-realm/users";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(adminCliToken);
        HttpEntity<UserRegistration> request = new HttpEntity<>(userRegistration, headers);
        ResponseEntity<String> stringResponseEntity = null;

        try {
            stringResponseEntity = restTemplate.postForEntity(url, request, String.class);
        } catch (HttpClientErrorException e) {
            if(e.getMessage().contains("User exists with same username"))
            return new ResponseEntity<>("User exists with same username", HttpStatus.CONFLICT);
        }

        return stringResponseEntity;
    }

    @GetMapping("/login")
    public ResponseEntity<String> login(@RequestBody UserLogin userLogin) throws JSONException {
        String url = "/realms/spring-boot-microservices-realm/protocol/openid-connect/token";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("grant_type", "password");
        map.add("client_id", "spring-cloud-client");
        map.add("scope", "openid");
        map.add("username", userLogin.getUsername());
        map.add("password", userLogin.getPassword());

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(map, headers);

        ResponseEntity<String> response = null;

        try {
            response = restTemplate.postForEntity(url, request, String.class);
        } catch (HttpClientErrorException e) {
            return new ResponseEntity<>("Invalid user credentials", HttpStatus.UNAUTHORIZED);
        }

        String responseBody = response.getBody();
        JSONObject jsonObj = new JSONObject(responseBody);
        String access_token = jsonObj.getString("access_token");
        return new ResponseEntity<>("{\"access_token\": \"" + access_token + "\"}", HttpStatus.OK);
    }

    public String getAdminCliToken() throws JSONException {
        String url = "/realms/spring-boot-microservices-realm//protocol/openid-connect/token";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("grant_type", "client_credentials");
        map.add("client_id", "admin-cli");
        map.add("client_secret", clientSecret);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(map, headers);

        ResponseEntity<String> response = restTemplate.postForEntity(url, request, String.class);

        String responseBody = response.getBody();
        JSONObject jsonObj = new JSONObject(responseBody);
        return jsonObj.getString("access_token");
    }
}
