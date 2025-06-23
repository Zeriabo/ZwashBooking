package com.zwash.booking.controller;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import com.zwash.booking.config.KafkaTopicConfig;
import com.zwash.booking.service.BookingService;
import com.zwash.common.dto.BookingDTO;
import com.zwash.common.exceptions.CarDoesNotExistException;
import com.zwash.common.exceptions.UserIsNotFoundException;
import com.zwash.common.grpc.CarRequest;
import com.zwash.common.grpc.CarResponse;
import com.zwash.common.grpc.CarServiceGrpc;
import com.zwash.common.grpc.CarWashingProgramResponse;
import com.zwash.common.grpc.CarWashingProgramServiceGrpc;
import com.zwash.common.grpc.GetWashingProgramRequest;
import com.zwash.common.grpc.ServiceProviderResponse;
import com.zwash.common.grpc.StationRequest;
import com.zwash.common.grpc.StationResponse;
import com.zwash.common.grpc.StationServiceGrpc;
import com.zwash.common.grpc.TokenRequest;
import com.zwash.common.grpc.UserResponse;
import com.zwash.common.grpc.UserServiceGrpc;
import com.zwash.common.pojos.Booking;
import com.zwash.common.pojos.Car;
import com.zwash.common.pojos.CarWashingProgram;
import com.zwash.common.pojos.FoamCarWashingProgram;
import com.zwash.common.pojos.HighPressureCarWashingProgram;
import com.zwash.common.pojos.ServiceProvider;
import com.zwash.common.pojos.Station;
import com.zwash.common.pojos.TouchlessCarWashingProgram;
import com.zwash.common.pojos.User;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.transaction.Transactional;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("v1/bookings")
@Tag(name = "Booking API")
public class BookingController {

	@Autowired
	private KafkaTemplate<String, String> kafkaTemplate;
	@Autowired
	private BookingService bookingService;

	@Autowired
	private UserServiceGrpc.UserServiceBlockingStub userStub;
	@Autowired
	private StationServiceGrpc.StationServiceBlockingStub stationStub;
	@Autowired
	private CarServiceGrpc.CarServiceBlockingStub carServiceStub;
	@Autowired
	private CarWashingProgramServiceGrpc.CarWashingProgramServiceBlockingStub carWashingProgramStub;

	Logger logger = LoggerFactory.getLogger(BookingController.class);

	@GetMapping(value = "/{id}")
	@Operation(summary = "Get a booking by ID")
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Successfully retrieved booking"),
			@ApiResponse(responseCode = "404", description = "Booking not found") })
	public ResponseEntity<Booking> getBooking(@PathVariable Long id) {
		Booking booking = bookingService.getBookingById(id);
		if (booking != null) {
			return new ResponseEntity<>(booking, HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}

	@GetMapping
	@Operation(summary = "Get all bookings")
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Successfully retrieved bookings"),
			@ApiResponse(responseCode = "404", description = "Bookings not found") })
	public ResponseEntity<List<BookingDTO>> getAllBookings() throws Exception {
		try {
			List<BookingDTO> bookingList = bookingService.getAllBookings();
			return new ResponseEntity<>(bookingList, HttpStatus.OK);
		} catch (Exception ex) {
			System.out.println(ex);
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}

	@GetMapping("/user/{id}")
	@Operation(summary = "Get bookings belong to a User")
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Successfully retrieved bookings"),
			@ApiResponse(responseCode = "404", description = "Bookings not found") })
	public ResponseEntity<List<BookingDTO>> getUsersBookings(@PathVariable("id") Long userId,
			@RequestBody(required = false) String reqJson) throws Exception {
		try {
			kafkaTemplate.send(KafkaTopicConfig.getUserTopic().name(), userId.toString());

			UserResponse userResponse = userStub.getUserById(
				    com.zwash.common.grpc.UserIdRequest.newBuilder().setId(userId).build()
				);
			User user = new User();
			user.setId(userResponse.getId());
			user.setFirstName(userResponse.getFirstName());
			user.setActive(userResponse.getActive());
			user.setAdmin(userResponse.getAdmin());
			user.setLastName(userResponse.getLastName());
			user.setToken(userResponse.getToken());
         
			List<BookingDTO> list = bookingService.getBookingsByUser(user);
			return new ResponseEntity<>(list, HttpStatus.OK);

		} catch (ExecutionException e) {
			if (e.getCause() instanceof UserIsNotFoundException) {
				return new ResponseEntity<>(HttpStatus.NOT_FOUND);
			}
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	private User getUserFromKafkaMessage(String reqJson) throws UserIsNotFoundException {
		// Parse reqJson and extract user information
		// Implement your logic to retrieve the user from the message payload
		// If user is not found, throw UserIsNotFoundException
		// If user is found, return the user object
		System.out.println(reqJson);
		return null;
	}

	@PostMapping
	@Transactional
	@Operation(summary = "Create a booking")
	@ApiResponses(value = { @ApiResponse(responseCode = "201", description = "Successfully created booking"),
			@ApiResponse(responseCode = "400", description = "Invalid request") })
	public ResponseEntity<Booking> createBooking(@RequestBody BookingDTO bookingDto) throws Exception {
		if (bookingDto == null)
			throw new IllegalArgumentException("Booking cannot be null");

		TokenRequest tokenRequest = TokenRequest.newBuilder().setToken(bookingDto.getToken()).build();
		UserResponse userResponse;
		try {
		    userResponse = userStub.getUserFromToken(tokenRequest);
		} catch (Exception e) {
		    logger.error("Failed to get user from token", e);
		    throw e;
		}
		User user = new User();
		user.setId(userResponse.getId());
		user.setUsername(userResponse.getUsername());
		user.setFirstName(userResponse.getFirstName());
		user.setLastName(userResponse.getLastName());
		user.setDateOfBirth(userResponse.getDateOfBirth());
		user.setActive(userResponse.getActive());
		user.setAdmin(userResponse.getAdmin());
		user.setToken(userResponse.getToken());

		StationResponse stationResponse = stationStub
				.getStation(StationRequest.newBuilder().setId(bookingDto.getStationId()).build());
		ServiceProviderResponse serviceProviderResponse = stationResponse.getServiceProvider();
		ServiceProvider serviceProvider = new ServiceProvider();
		serviceProvider.setId(serviceProviderResponse.getId());
		serviceProvider.setName(serviceProviderResponse.getName());
		serviceProvider.setEmail(serviceProviderResponse.getEmail());

		Station station = new Station(stationResponse.getName(), stationResponse.getAddress(),
				stationResponse.getLatitude(), stationResponse.getLongitude(), serviceProvider);
		CarResponse carResponse = carServiceStub.getCar(CarRequest.newBuilder().setId(bookingDto.getCarId()).build());
		Car car = new Car();
		car.setCarId(carResponse.getId());
		car.setRegisterationPlate(carResponse.getLicensePlate());
		car.setUser(user);

		// 1. Build the GetWashingProgramRequest
		GetWashingProgramRequest request = GetWashingProgramRequest.newBuilder().setId(bookingDto.getWashingProgramId())
				.build();

		// 1. Build the GetWashingProgramRespose
		CarWashingProgramResponse progRes = carWashingProgramStub.getProgramById(request);

		CarWashingProgram program;
		switch (progRes.getProgramType()) {
		case "foam" -> program = new FoamCarWashingProgram();
		case "high_pressure" -> program = new HighPressureCarWashingProgram();
		case "touch_less" -> program = new TouchlessCarWashingProgram();
		default -> throw new IllegalArgumentException("Unknown program type");
		}
		program.setId(progRes.getId());
		program.setProgramType(progRes.getProgramType());
		program.setDescription(progRes.getDescription());
		program.setPrice(progRes.getPrice());

		Booking booking = new Booking();
		booking.setUser(user);
		booking.setCar(car);
		booking.setStation(station);
		booking.setWashingProgram(program);
		booking.setToken(userResponse.getToken());

		Booking savedBooking = bookingService.saveBooking(booking);

		// FCM Notification
		if (user.getToken() != null && !user.getToken().isBlank()) {
			Message message = Message.builder()
					.setNotification(Notification.builder().setTitle("Booking Confirmed 🚗")
							.setBody("Wash booked for: " + car.getRegisterationPlate()).build())
					.setToken(user.getToken()).build();
			try {
				FirebaseMessaging.getInstance().send(message);
			} catch (FirebaseMessagingException e) {
				logger.error("FCM Error", e);
			}
		}

		return new ResponseEntity<>(savedBooking, HttpStatus.CREATED);
	}

	@PutMapping("/{id}")
	@Transactional
	@Operation(summary = "Update an existing booking")
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Booking updated successfully"),
			@ApiResponse(responseCode = "400", description = "Invalid request. Check input parameters"),
			@ApiResponse(responseCode = "404", description = "Booking with provided id not found") })
	public ResponseEntity<Booking> updateBooking(@PathVariable Long id, @RequestBody Booking newBooking) {
		if (newBooking == null) {
			throw new IllegalArgumentException("Booking object cannot be null");
		}
		if (newBooking.getStation() == null) {
			throw new IllegalArgumentException("Station object cannot be null");
		}
		if (newBooking.getCar() == null) {
			throw new IllegalArgumentException("Car object cannot be null");
		}
		if (newBooking.getWashingProgram() == null) {
			throw new IllegalArgumentException("Washing program object cannot be null");
		}

		Booking booking = null;// bookingService.getBookingById(id);
		if (booking != null) {
			booking.setCar(newBooking.getCar());
			booking.setWashingProgram(newBooking.getWashingProgram());
			// bookingService.saveBooking(booking);
			logger.info(
					"the  booking for " + booking.getCar().getRegisterationPlate() + " has been updated successfully");
			// Construct the message
			Message message = Message.builder()
					.setNotification(Notification.builder().setTitle("Booking made!")
							.setBody("You have updated a booking for car: " + booking.getCar().getRegisterationPlate())
							.build())
					.setToken("DEVICE_REGISTRATION_TOKEN")// to get from the react native app
					.build();

			// Send the message
			try {
				String response = FirebaseMessaging.getInstance().send(message);
				System.out.println("Successfully sent message: " + response);
			} catch (FirebaseMessagingException e) {
				System.out.println("Failed to send message: " + e.getMessage());
			}
			return new ResponseEntity<>(booking, HttpStatus.OK);
		} else {
			logger.error("Booking with id " + id + " not found");
			throw new IllegalArgumentException("Booking with id " + id + " not found");
		}
	}

	@SuppressWarnings("null")
	@PostMapping("/{id}")
	@Transactional
	@Operation(summary = "Execute a Wash")
	@ApiResponses(value = { @ApiResponse(responseCode = "204", description = "Wash executed successfully"),
			@ApiResponse(responseCode = "404", description = "Booking with provided id not found") })
	public ResponseEntity<Void> executeBookingWash(@PathVariable Long id) {
		Booking booking = null;// bookingService.getBookingById(id);
		if (booking != null) {
			// carWashService.executeCarWash(booking);

			logger.info(
					"the  booking for " + booking.getCar().getRegisterationPlate() + " has been executed successfully");

			return new ResponseEntity<>(HttpStatus.ACCEPTED);
		} else {
			logger.error("Booking with car " + booking.getCar().getRegisterationPlate() + " not executed!");

			throw new IllegalArgumentException("Booking with id " + id + " not found");
		}
	}

	@SuppressWarnings("null")
	@DeleteMapping("/{id}")
	@Transactional
	@Operation(summary = "Delete a booking by id")
	@ApiResponses(value = { @ApiResponse(responseCode = "204", description = "Booking deleted successfully"),
			@ApiResponse(responseCode = "404", description = "Booking with provided id not found") })
	public ResponseEntity<Void> deleteBooking(@PathVariable Long id) {
		Booking booking = null;// bookingService.getBookingById(id);
		if (booking != null) {
			logger.info(
					"the  booking for " + booking.getCar().getRegisterationPlate() + " has been deleted successfully");

			// bookingService.deleteBooking(booking);
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		} else {
			logger.error("Booking with car " + booking.getCar().getRegisterationPlate() + " not deleted!");

			throw new IllegalArgumentException("Booking with id " + id + " not found");
		}
	}

	@GetMapping("validate/{registrationPlate}")
	@ResponseStatus(HttpStatus.OK)
	@Operation(summary = "Check if a booking exists for a given car registration plate")
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Request processed successfully"),
			@ApiResponse(responseCode = "404", description = "Car with provided registration plate not found") })
	public ResponseEntity<Boolean> isBookingExistsForCar(@PathVariable String registrationPlate)
			throws CarDoesNotExistException {
		// Car car =null;
		// carService.getCar(registrationPlate);
		// Car not found
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(false);

	}

	@ExceptionHandler(IllegalArgumentException.class)
	public ResponseEntity<String> handleIllegalArgumentException(IllegalArgumentException ex) {
		return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<String> handleException(Exception ex) {
		return new ResponseEntity<>("An unexpected error occurred: " + ex.getMessage(),
				HttpStatus.INTERNAL_SERVER_ERROR);
	}

	private Station mapStationResponseToStation(StationResponse response) {
		Station station = new Station();
		station.setId(response.getId());
		station.setName(response.getName());
		station.setAddress(response.getAddress());
		station.setLatitude(response.getLatitude());
		station.setLongitude(response.getLongitude());

		return station;
	}

	private Car mapCarResponseToCar(CarResponse response) throws Exception {
		Car car = new Car();
		car.setCarId(response.getId());
		car.setManufacture(response.getBrand());
		car.setRegisterationPlate(response.getLicensePlate());
//	    car.setYear(response.get);
		// map other relevant fields...
		return car;
	}
}