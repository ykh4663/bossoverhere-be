package com.dontgojunbao.bossoverhere;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class BossoverhereBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(BossoverhereBackendApplication.class, args);
	}

}
