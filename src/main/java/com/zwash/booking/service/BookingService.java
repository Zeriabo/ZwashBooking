package com.zwash.booking.service;


import java.sql.SQLException;
import java.util.List;

import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import com.zwash.common.dto.BookingDTO;
import com.zwash.common.pojos.Booking;
import com.zwash.common.pojos.User;



@Service
public interface BookingService {
    Booking saveBooking(Booking booking);
    Booking getBookingById(Long id);
    List<Booking> getBookingsByUserId(Long userId) throws Exception;
    List<BookingDTO> getBookingsByUser(User user) throws Exception;
    List<Booking> getBookingsByCarId(Long carId);
	boolean isBookingExistsForCar(String registrationPlate);
	List<BookingDTO> getAllBookings() throws DataAccessException, SQLException, Exception;
	List<Booking> getAllBooking() throws DataAccessException, SQLException, Exception;
	boolean deleteBooking(Booking booking);
	Booking moveToWash(String carRegisterationPlate);

}
