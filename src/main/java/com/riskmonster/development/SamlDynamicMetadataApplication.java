package com.riskmonster.development;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.github.ulisesbocchio.spring.boot.security.saml.annotation.EnableSAMLSSO;

@SpringBootApplication
@EnableSAMLSSO
public class SamlDynamicMetadataApplication {

	public static void main(String[] args) {
		SpringApplication.run(SamlDynamicMetadataApplication.class, args);
	}

}
