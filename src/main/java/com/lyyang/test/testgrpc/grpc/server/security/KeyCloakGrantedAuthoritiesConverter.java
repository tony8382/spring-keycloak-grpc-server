package com.lyyang.test.testgrpc.grpc.server.security;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.stream.Collectors;

@Slf4j
@Service
public class KeyCloakGrantedAuthoritiesConverter implements Converter<Jwt, Collection<GrantedAuthority>> {

    private static final String SCOPE_AUTHORITY_PREFIX = "ROLE_";

    @Override
    public Collection<GrantedAuthority> convert(Jwt jwt) {
        return getScopes(jwt)
                .stream()
                .map(authority -> SCOPE_AUTHORITY_PREFIX + authority.toUpperCase())
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }


    private Collection<String> getScopes(Jwt jwt) {
        Collection<String> result = new ArrayList<>();

        try {
            JsonArray grpcServerRoles = JsonParser
                    .parseString(jwt.getClaims().get("resource_access").toString())
                    .getAsJsonObject()
                    .getAsJsonObject("grpc-server")
                    .getAsJsonArray("roles");

            for (JsonElement role : grpcServerRoles) {
                result.add(role.getAsString());
            }

        } catch (NullPointerException npe) {
            log.info("Can not find roles in Claims");
        }

        return result;
    }

}