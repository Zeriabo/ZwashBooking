package com.zwash.booking.service;

import org.springframework.stereotype.Service;

import com.zwash.booking.pojos.Booking;



@Service
public interface CarWashService {

	void executeCarWash(Booking booking);
}
