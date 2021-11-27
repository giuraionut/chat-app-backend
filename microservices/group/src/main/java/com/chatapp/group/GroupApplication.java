package com.chatapp.group;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication
@EnableEurekaClient
public class GroupApplication {

	public static void main(String[] args) {
		SpringApplication.run(GroupApplication.class, args);
	}

}
