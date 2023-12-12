package com.zwash.booking.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EnableJpaRepositories(basePackages = "com.zwash.common.repository")
public class JpaBookingConfig {
    // Any additional configuration you may need.
}
