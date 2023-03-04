package com.lyyang.test.testgrpc.grpc.server.security;


import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.server.security.authentication.BearerAuthenticationReader;
import net.devh.boot.grpc.server.security.authentication.GrpcAuthenticationReader;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.server.resource.BearerTokenAuthenticationToken;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationProvider;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Configuration(proxyBeanMethods = false)
@EnableGlobalMethodSecurity(securedEnabled = true, proxyTargetClass = true)
public class SecurityConfiguration {

    @Bean
    JwtAuthenticationConverter jwtAuthenticationConverter(
            final KeyCloakGrantedAuthoritiesConverter keyCloakGrantedAuthoritiesConverter) {

        final JwtAuthenticationConverter converter = new JwtAuthenticationConverter();
        converter.setJwtGrantedAuthoritiesConverter(keyCloakGrantedAuthoritiesConverter);
        return converter;
    }

    @Bean
    JwtAuthenticationProvider jwtAuthenticationProvider(final JwtAuthenticationConverter jwtAuthenticationConverter,
                                                        final JwtDecoder jwtDecoder) {
        final JwtAuthenticationProvider provider = new JwtAuthenticationProvider(jwtDecoder);
        provider.setJwtAuthenticationConverter(jwtAuthenticationConverter);
        return provider;
    }

    @Bean
    AuthenticationManager authenticationManager(final JwtAuthenticationProvider jwtAuthenticationProvider) {
        final List<AuthenticationProvider> providers = new ArrayList<>();
        providers.add(jwtAuthenticationProvider);
        return new ProviderManager(providers);
    }

    @Bean
    GrpcAuthenticationReader authenticationReader() {
        return new BearerAuthenticationReader(BearerTokenAuthenticationToken::new);
    }

    @Bean
    JwtDecoder jwtDecoder() {
        final String endpointURI = "http://localhost:8080/auth/realms/tony-test/protocol/openid-connect/certs";
        return NimbusJwtDecoder.withJwkSetUri(endpointURI).build();
    }

}