package com.zwash.booking.mapper;


import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.zwash.booking.grpc.CarServiceGrpcClient;
import com.zwash.booking.service.CarWashingProgramService;
import com.zwash.common.dto.BookingDTO;
import com.zwash.common.exceptions.CarDoesNotExistException;
import com.zwash.common.exceptions.UserIsNotFoundException;
import com.zwash.common.pojos.Booking;
import com.zwash.common.pojos.Car;
import com.zwash.common.pojos.CarWashingProgram;



@Component
public class BookingMapperImpl implements BookingMapper {

	@Autowired
    private CarServiceGrpcClient carServiceGrpcClient;


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
        Car car;
        try {
            car = carServiceGrpcClient.getCar(bookingDTO.getCarId());
        } catch (Exception e) {
            throw new CarDoesNotExistException("Car with ID " + bookingDTO.getCarId() + " does not exist.");
        }
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
			} catch (UserIsNotFoundException e) {

				e.printStackTrace();
			} catch (CarDoesNotExistException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return null;
		}).collect(Collectors.toList());
    }
}