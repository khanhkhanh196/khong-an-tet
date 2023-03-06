package com.microservices.apigateway.config.jwt;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class JWTInformation {
    private String username;
    private boolean exp;

}
