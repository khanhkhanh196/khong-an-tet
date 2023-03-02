package com.microservices.apigateway.object;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class UserRegistration {
    private String username;
    private String firstName;
    private String lastName;
    private String email;
    private boolean enabled;
    private boolean emailVerified;
    private List<Credential> credentials;
}
