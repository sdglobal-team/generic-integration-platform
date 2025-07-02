package com.generic.integration;

import com.arangodb.springframework.annotation.EnableArangoRepositories;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class GenericIntegrationApplication {

	public static void main(String[] args) {
		SpringApplication.run(GenericIntegrationApplication.class, args);
	}

}
