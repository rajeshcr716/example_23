package com.eazybytes.eazyschool;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import javax.persistence.Entity;

// next_part exampl_22

@SpringBootApplication
@EnableJpaRepositories("com.eazybytes.eazyschool.repository")  // need enable the repositary so we mention repositary path.
@EntityScan("com.eazybytes.eazyschool.model") // need to mention where my pojo class available
@EnableJpaAuditing(auditorAwareRef = "auditAwareImpl")
public class EazyschoolApplication {

	public static void main(String[] args) {
		SpringApplication.run(EazyschoolApplication.class, args);
	}

}
