package com.chatapp.directmessage;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties
public class DirectMessageApplication {

	public static void main(String[] args) {
		SpringApplication.run(DirectMessageApplication.class, args);
	}

}
