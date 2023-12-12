package com.zwash.booking.service;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.NoSuchElementException;

import org.springframework.stereotype.Service;

import com.zwash.booking.pojos.Wash;



@Service
public interface WashService extends Serializable{

	Wash  getWash(Long id) throws NoSuchElementException;

	Wash getWashByBooking(Long bookingId) throws NoSuchElementException;

	boolean startWash(Wash wash);

	boolean finishWash(Wash wash);

	boolean cancelWash(Wash wash);

	boolean rescheduleWash(Wash wash, LocalDateTime startTime);

	boolean buyWash(Wash wash);

}
