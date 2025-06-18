package com.zwash.booking.mapper;

import java.util.List;
import java.util.stream.Collectors;

import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

import com.zwash.auth.exceptions.UserIsNotFoundException;
import com.zwash.common.dto.BookingDTO;
import com.zwash.booking.factory.CarWashingProgramFactory;
import com.zwash.car.exceptions.CarDoesNotExistException;
import com.zwash.common.pojos.Booking;
import com.zwash.common.pojos.CarWashingProgram;




public interface BookingMapper {

    BookingMapper INSTANCE = Mappers.getMapper(BookingMapper.class);

    @Mapping(source = "car.carId", target = "carId")
    @Mapping(source = "washingProgram.id", target = "washingProgramId")
    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "token", target = "token")
    @Mapping(source = "executed", target = "executed")
    BookingDTO toBookingDto(Booking booking);

    @Mapping(source = "carId", target = "car.carId")
    @Mapping(source = "userId", target = "user.id")
    @Mapping(source = "washingProgramId", target = "washingProgram", qualifiedByName = "createCarWashingProgram")
    @Mapping(source = "scheduledTime", target = "scheduledTime")
    @Mapping(source = "token", target = "token")
    @Mapping(source = "executed", target = "executed")
    Booking toBooking(BookingDTO bookingDTO) throws UserIsNotFoundException, CarDoesNotExistException;

    @Named("createCarWashingProgram")
    default CarWashingProgram createCarWashingProgram(String washingProgramId) {
        return CarWashingProgramFactory.createCarWashingProgram(washingProgramId);
    }
    default List<BookingDTO> toBookingDtoList(List<Booking> bookings) {
        return bookings.stream().map(this::toBookingDto).collect(Collectors.toList());
    }

    default List<Booking> toBookingList(List<BookingDTO> bookingDtos) {
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
