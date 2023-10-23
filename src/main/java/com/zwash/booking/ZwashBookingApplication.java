package com.zwash.booking;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;


@ComponentScan(basePackages = {"com.zwash.common"})
@SpringBootApplication
public class ZwashBookingApplication {

	public static void main(String[] args) {
		SpringApplication.run(ZwashBookingApplication.class, args);
	}

}
