package com.microservices.apigateway.object;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Credential {
    private String type;
    private String value;
    private boolean temporary;
}
