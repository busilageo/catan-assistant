package com.gt.catanassistant;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class })
public class CatanAssistantApplication {

	public static void main(String[] args) {
		SpringApplication.run(CatanAssistantApplication.class, args);
	}

}
