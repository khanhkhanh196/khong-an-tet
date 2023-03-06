package com.microservices.apigateway.config.jwt;
import com.auth0.jwk.InvalidPublicKeyException;
import com.auth0.jwk.Jwk;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.DecodedJWT;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.security.jwt.JwtHelper;
import org.springframework.stereotype.Service;

import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;

@Service
@Slf4j
@PropertySource("classpath:application.properties")
public class JwtVerifySignature{
    private static final String PUBLIC_KEY_NOT_AVAILABLE = "public key not available";
    @Value("${spring.security.oauth2.resourceserver.jwt.issuer-uri}")
    private String issuer;
    @Value("${keycloak.jwt.jwk-url}")
    private String wellknowURL;
    @Autowired
    private JwtRetrieveService jwkService;

    public JWTVerifier getJWTVerifier(String jwtStr, String kid) throws JWTVerificationException {
        JWTVerifier verifier = null;

        //custom process to define Auth server JWK set URL should be implemented

        Jwk jwk = jwkService.retrieveJwk(kid, wellknowURL).orElseThrow(
                () -> new JWTVerificationException(PUBLIC_KEY_NOT_AVAILABLE));

        try {
            //Create algortihm using the JWK public key
            Algorithm alg = Algorithm.RSA256((RSAPublicKey) jwk.getPublicKey(), null);

            //Create verifier using algorithm
            verifier = JWT.require(alg).withIssuer(issuer).build(); //dynamic mechanism to obtain expected JWT issuer
        } catch (InvalidPublicKeyException e) {
            throw new RuntimeException(e);
        }

        return verifier;
    }

    public void performSignatureVerification(String jwtStr,
                                              JWTVerifier verifier ) throws JWTVerificationException {
        try {

            DecodedJWT decodedJwt = verifier.verify(jwtStr);

            //successful JwtVerification

            log.info("JWT token successfully verified...");
            log.debug("JWT token claims: {}", decodedJwt.getClaims());

        } catch (TokenExpiredException e) {
            log.error("Invalid token received: expired.", e);
            throw new JWTVerificationException("EXPIRED_REQUEST");
        } catch (JWTVerificationException e) {
            log.error("Failed to verify JWT using provided secret key...", e);
            throw new JWTVerificationException("INVALID_SIGNATURE");
        }
    }
}
