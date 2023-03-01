package com.microservices.apigateway.config;

import com.microservices.apigateway.config.jwt.JwtDecoder;
import com.microservices.apigateway.config.jwt.JwtFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.web.server.WebFilter;

import javax.servlet.Filter;

@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {
    @Bean
    public SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity serverHttpSecurity) {
        serverHttpSecurity
                .csrf().and().cors().disable()
                .authorizeExchange(exchange ->
                        exchange.pathMatchers("/eureka/**")
                                .permitAll()
                                .anyExchange()
                                .authenticated())
                .oauth2ResourceServer(ServerHttpSecurity.OAuth2ResourceServerSpec::jwt);
//                .addFilterAfter(jwtFilter(), SecurityWebFiltersOrder.SERVER_REQUEST_CACHE);

        return serverHttpSecurity.build();
    }

    @Bean
    public JwtDecoder jwtAuthenticationFilter() {
        return new JwtDecoder();
    }
//    @Bean
//    public WebFilter jwtFilter() {
//        return new JwtFilter();
//    }
}
