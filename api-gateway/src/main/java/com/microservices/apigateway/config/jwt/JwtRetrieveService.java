package com.microservices.apigateway.config.jwt;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;

import com.auth0.jwk.Jwk;
import com.auth0.jwk.JwkException;
import com.auth0.jwk.JwkProvider;
import com.auth0.jwk.UrlJwkProvider;
@Service
@Slf4j
public class JwtRetrieveService {
    private final Map<String, Jwk> cacheJwkStore = new HashMap<String, Jwk>();

    public Optional<Jwk> retrieveJwk(String kid, String keysetUrl) {
        Jwk jwk = null;

        //https://www.imsglobal.org/spec/security/v1p0/#h_key-set-url
        //kid is REQUIRED even when there is only a single public key, if not found ,expect explicit declaration in the platform record
        if (StringUtils.isEmpty(kid)) {
            log.warn("invalid 'kid' header: [{}], it is required to define the Platform public rey.", kid);

        } else if (StringUtils.isEmpty(keysetUrl) ) {
            log.warn("invalid keysetUrl [{}], unable to fetch JWK");

        } else {
            try {
                jwk = cacheJwkStore.get(kid);

                //if cached JWK is not found, or it is expired, retrieve JWK and store it in cache map
                if (Objects.isNull(jwk) || isJwkExpired(jwk)) {

                    log.debug("JWK for id {} NOT found or expired in internal cache: {}, retrieving from keyset URL [{}]...", kid, jwk, keysetUrl);
                    URL url = new URL(keysetUrl);
                    JwkProvider jwkProvider = new UrlJwkProvider(url);

                    jwk = jwkProvider.get(kid);

                    //cache the JWK
                    cacheJwkStore.put(kid, jwk);

                    log.debug("JWK for id {} successfully retrieved from keyset URL and stored in internal cache...", kid);
                }
                else {
                    log.debug("JWK for id {} retrieved from internal cache store...", kid);
                }
            } catch (MalformedURLException e) {
                log.error("Invalid URL: {}, unable to obtain Public key to perform token verification",
                        keysetUrl, e);
            } catch (JwkException e) {
                log.error("Unable to define JWK for kid: {}, using keyset url {}", kid, keysetUrl, e);
            }
        }

        return Optional.ofNullable(jwk);
    }

    private boolean isJwkExpired(Jwk jwk) {
        Optional<Calendar> optExpDate = getExpirationDate(jwk);

        boolean expired = optExpDate.isPresent()
                && optExpDate.get().before(getUtcCalendar());

        return expired;
    }

    private Optional<Calendar> getExpirationDate(Jwk jwk) {
        Object expObj = jwk.getAdditionalAttributes().get("exp");

        Calendar expCal = null;
        if (Objects.nonNull(expObj)) {
            expCal = getUtcCalendar();
            //"exp expressed in seconds, need to convert to milliseconds
            expCal.setTimeInMillis(Long.valueOf(expObj.toString())*1000);
        }

        return Optional.ofNullable(expCal);
    }

    private Calendar getUtcCalendar() {
        return Calendar.getInstance(TimeZone.getTimeZone("UTC"));
    }
}
