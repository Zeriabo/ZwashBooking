package com.zwash.booking;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableEurekaClient
public class ZwashBookingApplication {

	public static void main(String[] args) {
		SpringApplication.run(ZwashBookingApplication.class, args);
	}

}
	