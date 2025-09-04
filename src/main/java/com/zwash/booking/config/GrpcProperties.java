package com.zwash.booking.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "grpc")
public class GrpcProperties {

    private Service bookingService;
    private Service stationService;
    private Service carService;
    private Service userService;

    public static class Service {
        private String host;
        private int port;

        public String getHost() { return host; }
        public void setHost(String host) { this.host = host; }

        public int getPort() { return port; }
        public void setPort(int port) { this.port = port; }
    }

    public Service getBookingService() { return bookingService; }
    public void setBookingService(Service bookingService) { this.bookingService = bookingService; }

    public Service getStationService() { return stationService; }
    public void setStationService(Service stationService) { this.stationService = stationService; }

    public Service getCarService() { return carService; }
    public void setCarService(Service carService) { this.carService = carService; }

    public Service getUserService() { return userService; }
    public void setUserService(Service userService) { this.userService = userService; }
}
