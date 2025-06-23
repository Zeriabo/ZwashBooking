package com.zwash.booking.config;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackages = {
	"com.zwash.booking.serviceImpl",
    "com.zwash.booking.repository",
    "com.zwash.common",
    "com.zwash.auth",
})
@EntityScan(basePackages = {"com.zwash.common.pojos","com.zwash.booking.pojos"})
public class AppConfig {
    // Configuration beans if any
}
