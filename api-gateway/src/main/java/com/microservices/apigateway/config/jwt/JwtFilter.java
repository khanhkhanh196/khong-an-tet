package com.microservices.apigateway.config.jwt;

import com.auth0.jwt.JWTVerifier;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class JwtFilter {
//        implements WebFilter {

    @Autowired
    JwtVerifySignature verifySignature;
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        String jwt = getJwtFromRequest(request);
        String kid = getKid(jwt);
        JWTVerifier jwtVerifier = verifySignature.getJWTVerifier(jwt, kid);
        verifySignature.performSignatureVerification(jwt, jwtVerifier);
        return null;
    }
    private String getJwtFromRequest(ServerHttpRequest request) {
        HttpHeaders headers = request.getHeaders();
        List<String> authorization = headers.get("Authorization");
        String bearerToken = null;
        if(null != authorization && !authorization.isEmpty()) {
             bearerToken = authorization.get(0);
        }
        // Kiểm tra xem header Authorization có chứa thông tin jwt không
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer")) {
            return bearerToken.substring(7);
        }
        return null;
    }


    private String getKid(String token) {
        String[] parts = token.split("\\.");

        try {
            JSONObject header = new JSONObject(decode(parts[0]));
            String kid = header.getString("kid");
            return kid;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    private String decode(String encodedString) {
        return new String(Base64.getUrlDecoder().decode(encodedString));
    }
    private String encode(byte[] encodedString) {
        return new String(Base64.getUrlEncoder().encode(encodedString));
    }


}
