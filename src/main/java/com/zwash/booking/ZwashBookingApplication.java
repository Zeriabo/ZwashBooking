package com.zwash.booking;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;

@EntityScan(basePackages = {"com.zwash.common.pojos","com.zwash.booking.pojos"})
@ComponentScan(basePackages= { "com.zwash.booking.service"})
@SpringBootApplication
public class ZwashBookingApplication {

	public static void main(String[] args) {
		SpringApplication.run(ZwashBookingApplication.class, args);
	}

}
