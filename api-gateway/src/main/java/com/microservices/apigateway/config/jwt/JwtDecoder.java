package com.microservices.apigateway.config.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nimbusds.jwt.JWT;
import com.nimbusds.jwt.JWTParser;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.security.oauth2.jwt.ReactiveJwtDecoder;
import reactor.core.publisher.Mono;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;


public class JwtDecoder implements ReactiveJwtDecoder {
    @Autowired
    private ObjectMapper objectMapper;
    private String decoder(String encodedString) {
        return new String(Base64.getUrlDecoder().decode(encodedString));
    }

    private String encode(byte[] encodedString) {
        return new String(Base64.getUrlEncoder().encode(encodedString));
    }

    private String hmacSha256(String data, String secret) {
        try {

            byte[] hash = secret.getBytes(StandardCharsets.UTF_8);
            Mac sha256Hmac = Mac.getInstance("HmacSHA256");
            SecretKeySpec secretKey = new SecretKeySpec(hash, "HmacSHA256");
            sha256Hmac.init(secretKey);

            byte[] signedBytes = sha256Hmac.doFinal(data.getBytes(StandardCharsets.UTF_8));
            return encode(signedBytes);
        } catch (InvalidKeyException | NoSuchAlgorithmException ex) {
            Logger.getLogger(JwtDecoder.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
            return null;
        }
    }

    @Override
    public Mono<Jwt> decode(String token) throws JwtException {

        JWT jwt = null;

        try {
            jwt = JWTParser.parse(token);
        } catch (java.text.ParseException e) {
            throw new RuntimeException(e);
        }

        Jwt finalJwt;

        try {
            finalJwt= createJwt(token, jwt);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        return Mono.just(finalJwt);
    }

    private void getDetailFromJwt(String token) {
        String[] parts = token.split("\\.");

        try {
            JSONObject header = new JSONObject(decoder(parts[0]));
            JSONObject payload = new JSONObject(decoder(parts[1]));
            String userName = payload.getString("preferred_username");
            boolean expried = payload.getLong("exp") > (System.currentTimeMillis() / 1000);

        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

    private Jwt createJwt(String token, JWT parsedJwt) throws ParseException {
        Map<String, Object> headers = new LinkedHashMap<>(parsedJwt.getHeader().toJSONObject());
        Map<String, Object> claims = new HashMap<>();
        for (String key : parsedJwt.getJWTClaimsSet().getClaims().keySet()) {
            Object value = parsedJwt.getJWTClaimsSet().getClaims().get(key);
            if (key.equals("exp") || key.equals("iat")) {
                value = ((Date) value).toInstant();
            }
            claims.put(key, value);
        }
        return Jwt.withTokenValue(token)
                .headers(h -> h.putAll(headers))
                .claims(c -> c.putAll(claims))
                .build();
    }
}