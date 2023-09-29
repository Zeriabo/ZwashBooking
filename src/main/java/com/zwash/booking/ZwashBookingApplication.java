package com.zwash.booking;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;

@ComponentScan(basePackages = {"com.zwash.auth.service","com.zwash.booking.service","com.zwash.booking.controller","com.zwash.booking.config"})
@EntityScan(basePackages = {"com.zwash.common.pojos","com.zwash.booking.pojos"})
@SpringBootApplication
public class ZwashBookingApplication {

	public static void main(String[] args) {
		SpringApplication.run(ZwashBookingApplication.class, args);
	}

}
