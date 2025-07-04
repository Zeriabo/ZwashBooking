package com.zwash.booking.mapper;


import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.zwash.auth.exceptions.UserIsNotFoundException;
import com.zwash.car.exceptions.CarDoesNotExistException;
import com.zwash.car.service.CarService;
import com.zwash.auth.service.UserService;
import com.zwash.common.dto.BookingDTO;
import com.zwash.booking.service.CarWashingProgramService;
import com.zwash.common.pojos.Booking;
import com.zwash.common.pojos.Car;
import com.zwash.common.pojos.CarWashingProgram;



@Component
public class BookingMapperImpl implements BookingMapper {
	@Autowired
	CarService carService;

	@Autowired
	UserService userService;

	@Autowired
	CarWashingProgramService carWashingProgramService;

    @Override
    public BookingDTO toBookingDto(Booking booking) {
        if (booking == null) {
            return null;
        }
        BookingDTO bookingDTO = new BookingDTO();
        bookingDTO.setId(booking.getId());
        bookingDTO.setUserId(booking.getUser().getId());
        bookingDTO.setCarId(booking.getCar().getCarId());
        bookingDTO.setWashingProgramId(booking.getWashingProgram().getId());

        return bookingDTO;
    }

    @Override
    public Booking toBooking(BookingDTO bookingDTO) throws UserIsNotFoundException, CarDoesNotExistException {
        if (bookingDTO == null) {
            return null;
        }
        Booking booking = new Booking();
        booking.setId(bookingDTO.getId());
        Car car =carService.getCar(bookingDTO.getCarId());
        booking.setCar(car);
        CarWashingProgram carWashingProgram  = carWashingProgramService.getProgramById(bookingDTO.getWashingProgramId());
        booking.setWashingProgram(carWashingProgram);
        booking.getWashingProgram().setId(bookingDTO.getWashingProgramId());
        return booking;
    }

    @Override
    public List<BookingDTO> toBookingDtoList(List<Booking> bookings) {
        return bookings.stream().map(this::toBookingDto).collect(Collectors.toList());
    }

    @Override
    public List<Booking> toBookingList(List<BookingDTO> bookingDtos) {
        return bookingDtos.stream().map(t -> {
			try {
				return toBooking(t);
			} catch (UserIsNotFoundException | CarDoesNotExistException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return null;
		}).collect(Collectors.toList());
    }
}