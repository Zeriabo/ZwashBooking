package com.zwash.booking;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
@SpringBootApplication(scanBasePackages = {"com.zwash", "com.zwash.common"})
@EnableJpaRepositories(basePackages = "com.zwash.booking.repository")
public class ZwashBookingApplication {

	public static void main(String[] args) {
		SpringApplication.run(ZwashBookingApplication.class, args);
	}

}
