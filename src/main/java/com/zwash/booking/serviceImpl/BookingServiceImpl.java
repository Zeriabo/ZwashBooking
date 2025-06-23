package com.zwash.booking.serviceImpl;

import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import com.zwash.booking.mapper.BookingMapper;
import com.zwash.booking.service.BookingService;
import com.zwash.booking.service.WashService;
import com.zwash.common.dto.BookingDTO;
import com.zwash.common.pojos.Booking;
import com.zwash.common.pojos.Car;
import com.zwash.common.pojos.User;
import com.zwash.common.repository.BookingRepository;
import com.zwash.common.repository.CarRepository;

import jakarta.persistence.EntityNotFoundException;

@Service
public class BookingServiceImpl implements BookingService {


	@Autowired
	private final BookingRepository bookingRepository;

	@Autowired
	private final CarRepository carRepository;

	@Autowired
	private final WashService washService;

	  public BookingServiceImpl(BookingRepository bookingRepository, CarRepository carRepository,WashService washService) {
		this.bookingRepository = bookingRepository;
		this.carRepository = carRepository;
		this.washService = washService;


	}

	@Override
	public Booking saveBooking(Booking booking) {
		return bookingRepository.save(booking);
	}

	@Override
	public Booking getBookingById(Long id) {
		return bookingRepository.findById(id).get();
	}


	@Override
	public List<BookingDTO> getAllBookings() throws  Exception {
	    try {
	         List<Booking> bookings =bookingRepository.findAll();
	        return bookings.stream()
	        		.map(BookingMapper.INSTANCE::toBookingDto)//toBookingDtoList
	                .collect(Collectors.toList());
	    } catch (Exception ex) {
	        if (ex instanceof InvocationTargetException exception) {
	            Throwable targetException = exception.getCause();
	            System.out.println(targetException);
	        }
	        throw new Exception("Error occurred while getting all bookings", ex);
	    }

	}


	@Override
	public List<Booking> getBookingsByCarId(Long carId) {
		return bookingRepository.findByCarId(carId);
	}

	@Override
	public List<BookingDTO> getBookingsByUser(User user) throws Exception {
	    try {
	         List<Booking> bookings =bookingRepository.findByUser(user);
	        return bookings.stream()
	        		.map(BookingMapper.INSTANCE::toBookingDto)//toBookingDtoList
	                .collect(Collectors.toList());
	    } catch (Exception ex) {
	        if (ex instanceof InvocationTargetException exception) {
	            Throwable targetException = exception.getCause();
	            System.out.println(targetException);
	        }
	        throw new Exception("Error occurred while getting all bookings", ex);
	    }

	}

	@Override
	public boolean isBookingExistsForCar(String registrationPlate) {
		Car car = carRepository.findByRegisterationPlate(registrationPlate);
		if (car == null) {
			// Car not found
			throw new EntityNotFoundException("Car not found");
		}
		// Check if any booking exists for the car
		Booking booking = bookingRepository.findByCarAndExecuted(car.getCarId(), false);
		return booking==null;
	}

	@Override
	public boolean deleteBooking(Booking booking) {
		bookingRepository.delete(booking);
		return true;
	}

	@Override
	public Booking moveToWash(String registrationPlate) {

		Car car = carRepository.findByRegisterationPlate(registrationPlate);


        Booking booking = bookingRepository.findByCarAndExecuted(car.getCarId(), false);

        if (booking != null) {

            booking.setExecuted(true);
            bookingRepository.save(booking);
            washService.startWash(booking.getWash());


            return booking;
        } else {
            // Handle the case where no booking is found for the given registration plate
            throw new IllegalArgumentException("No non-executed booking found for registration plate: " + registrationPlate);
        }
    }

	@Override
	public List<Booking> getAllBooking() throws DataAccessException, SQLException, Exception {
	return 	bookingRepository.findAll();
	}

	@Override
	public List<Booking> getBookingsByUserId(Long userId) throws Exception {
	    try {
	         List<Booking> bookings =bookingRepository.findByUserId(userId);
	        return bookings;
	    } catch (Exception ex) {
	        if (ex instanceof InvocationTargetException exception) {
	            Throwable targetException = exception.getCause();
	            System.out.println(targetException);
	        }
	        throw new Exception("Error occurred while getting all bookings", ex);
	    }

	}
}
