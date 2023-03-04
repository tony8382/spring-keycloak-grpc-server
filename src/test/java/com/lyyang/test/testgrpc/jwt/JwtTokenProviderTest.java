package com.lyyang.test.testgrpc.jwt;

import com.lyyang.test.testgrpc.TestGrpcApplicationTests;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.ArrayList;
import java.util.List;

@Slf4j
class JwtTokenProviderTest extends TestGrpcApplicationTests {

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Test
    void createToken() {


        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
        authorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
        Authentication authentication = new UsernamePasswordAuthenticationToken("user", "", authorities);

        log.info("HIHI:{}", jwtTokenProvider.createToken(authentication));

    }


}