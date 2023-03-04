package com.lyyang.test.testgrpc;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;

@SpringBootApplication
public class TestGrpcApplication {

	public static void main(String[] args) {
		SpringApplication.run(TestGrpcApplication.class, args);
	}

}
