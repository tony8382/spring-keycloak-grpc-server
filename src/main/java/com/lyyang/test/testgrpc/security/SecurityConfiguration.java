package com.lyyang.test.testgrpc.security;


import com.lyyang.test.testgrpc.jwt.JwtGrantedAuthoritiesConverter;
import com.lyyang.test.testgrpc.jwt.JwtProperties;
import io.jsonwebtoken.SignatureAlgorithm;
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

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Configuration(proxyBeanMethods = false)
@EnableGlobalMethodSecurity(securedEnabled = true, proxyTargetClass = true)
public class SecurityConfiguration {

    @Bean
    JwtAuthenticationConverter jwtAuthenticationConverter(final JwtGrantedAuthoritiesConverter jwtGrantedAuthoritiesConverter) {

        final JwtAuthenticationConverter converter = new JwtAuthenticationConverter();
        converter.setJwtGrantedAuthoritiesConverter(jwtGrantedAuthoritiesConverter);
        return converter;
    }

    @Bean
    JwtAuthenticationProvider jwtAuthenticationProvider(final JwtAuthenticationConverter jwtAuthenticationConverter, final JwtDecoder jwtDecoder) {

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
    JwtDecoder jwtDecoder(final JwtProperties jwtProperties) {
        SecretKey originalKey = new SecretKeySpec(jwtProperties.getSignKey().getBytes(), SignatureAlgorithm.HS256.getJcaName());
        return NimbusJwtDecoder.withSecretKey(originalKey).build();
    }

}