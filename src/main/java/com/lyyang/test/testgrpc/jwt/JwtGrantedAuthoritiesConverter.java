package com.lyyang.test.testgrpc.jwt;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Slf4j
@Service
public class JwtGrantedAuthoritiesConverter implements Converter<Jwt, Collection<GrantedAuthority>> {

    private static final String AUTHORITIES_KEY = "roles";

    @Override
    public Collection<GrantedAuthority> convert(Jwt jwt) {
        Object authoritiesClaim = jwt.getClaims().get(AUTHORITIES_KEY);

        return authoritiesClaim == null ? AuthorityUtils.NO_AUTHORITIES : AuthorityUtils.commaSeparatedStringToAuthorityList(authoritiesClaim.toString());
    }

}