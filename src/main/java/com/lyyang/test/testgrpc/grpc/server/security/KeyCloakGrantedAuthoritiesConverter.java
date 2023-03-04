package com.lyyang.test.testgrpc.grpc.server.security;

import com.nimbusds.jose.shaded.json.JSONArray;
import com.nimbusds.jose.shaded.json.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
public class KeyCloakGrantedAuthoritiesConverter implements Converter<Jwt, Collection<GrantedAuthority>> {

    private static final String SCOPE_AUTHORITY_PREFIX = "ROLE_";
    private static final Collection<String> WELL_KNOWN_SCOPE_ATTRIBUTE_NAMES =
            Arrays.asList("realm_access");

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
        for (String attributeName : WELL_KNOWN_SCOPE_ATTRIBUTE_NAMES) {
            JSONObject realm_access = (JSONObject) jwt.getClaims().get(attributeName);
            if (Objects.isNull(realm_access)) {
                return Collections.emptyList();
            }

            JSONArray roles = (JSONArray) realm_access.get("roles");
            if (Objects.isNull(roles)) {
                return Collections.emptyList();
            }

            for (Object role : roles) {
                result.add((String) role);
            }
        }
        return result;
    }

}