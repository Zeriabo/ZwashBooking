package com.zwash.booking.controller;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import com.zwash.common.exceptions.CarDoesNotExistException;
import com.zwash.common.exceptions.UserIsNotFoundException;
import com.zwash.common.grpc.CarServiceGrpc;
import com.zwash.common.grpc.CarWashingProgramRequest;
import com.zwash.common.grpc.CarWashingProgramResponse;
import com.zwash.common.grpc.CarRequest;
import com.zwash.common.grpc.CarResponse;
import com.zwash.common.grpc.CarWashingProgramServiceGrpc;
import com.zwash.common.grpc.ServiceProviderServiceGrpc;
import com.zwash.common.grpc.StationRequest;
import com.zwash.common.grpc.StationResponse;
import com.zwash.common.grpc.StationServiceGrpc;
import com.zwash.common.grpc.TokenRequest;
import com.zwash.common.grpc.UserResponse;
import com.zwash.common.grpc.UserServiceGrpc;
import com.zwash.common.dto.BookingDTO;
import com.zwash.common.pojos.Station;
import com.zwash.common.pojos.TouchlessCarWashingProgram;
import com.zwash.booking.config.KafkaTopicConfig;
import com.zwash.booking.service.BookingService;
import com.zwash.common.pojos.Booking;
import com.zwash.common.pojos.Car;
import com.zwash.common.pojos.CarWashingProgram;
import com.zwash.common.pojos.FoamCarWashingProgram;
import com.zwash.common.pojos.HighPressureCarWashingProgram;
import com.zwash.common.pojos.User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.transaction.Transactional;

@Controller
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
	private ServiceProviderServiceGrpc.ServiceProviderServiceBlockingStub serviceProviderStub;

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

			CompletableFuture<User> userFuture = new CompletableFuture<>();
			// Set up a callback to handle the asynchronous user retrieval
			userFuture.thenApplyAsync(user -> {
				try {
					// If the user is null, handle the scenario (throw exception, return appropriate
					// response, etc.)
					if (user == null) {
						throw new UserIsNotFoundException("User not found for id: " + userId);
					}
					// If the user is found, get bookings and return the response
					List<BookingDTO> list = bookingService.getBookingsByUser(user);
					return new ResponseEntity<>(list, HttpStatus.OK);
				} catch (UserIsNotFoundException ex) {
					return new ResponseEntity<>(HttpStatus.NOT_FOUND);
				} catch (Exception ex) {
					return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
				}
			});

		} catch (Exception ex) {

			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<>(null, HttpStatus.OK);
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

		Booking booking = new Booking();

		if (bookingDto == null) {
			throw new IllegalArgumentException("Booking  cannot be null");
		}

		if (bookingDto.getToken() != null) {
			booking.setToken(bookingDto.getToken());
		}
		TokenRequest request = TokenRequest.newBuilder().setToken(bookingDto.getToken()).build();
		UserResponse userResponse = userStub.getUserFromToken(request);

		User user = new User();
		user.setId(userResponse.getId());
		user.setUsername(userResponse.getUsername());
		user.setActive(userResponse.getActive());
		user.setDateOfBirth(userResponse.getDateOfBirth());
		user.setFirstName(userResponse.getFirstName());
		user.setLastName(userResponse.getLastName());
		user.setAdmin(userResponse.getAdmin());

		booking.setUser(user);

		if (bookingDto.getStationId() == null) {
			throw new IllegalArgumentException("Station object cannot be null");
		}
		StationResponse stationResponse = stationStub
				.getStation(StationRequest.newBuilder().setId(bookingDto.getStationId()).build());
		Station station = mapStationResponseToStation(stationResponse);
		booking.setStation(station);

		if (bookingDto.getCarId() == null) {
			throw new IllegalArgumentException("Car object cannot be null");
		}

		CarResponse carResponse = carServiceStub.getCar(CarRequest.newBuilder().setId(bookingDto.getCarId()).build());

		// Map the CarResponse to your local Car entity/model
		Car car = mapCarResponseToCar(carResponse);

		if (car == null) {
			logger.error("The car " + booking.getCar().getRegisterationPlate() + "  is not registered in the system!");
			throw new IllegalArgumentException("There is no car in the system with registeration number "
					+ booking.getCar().getRegisterationPlate());
		}

		booking.setCar(car);

		if (bookingDto.getWashingProgramId() == null) {
			throw new IllegalArgumentException("Washing program object cannot be null");
		}
		CarWashingProgramRequest carWashingProgramRequest = CarWashingProgramRequest.newBuilder()
			    .setId(bookingDto.getWashingProgramId())
			    .build();

         CarWashingProgramResponse response = carWashingProgramStub.getCarWashingProgram(carWashingProgramRequest);


         CarWashingProgram washingProgram;
         switch (response.getProgramType()) {
         case "foam":
             washingProgram = new FoamCarWashingProgram();
             break;
         case "high_pressure":
             washingProgram = new HighPressureCarWashingProgram();
             break;
         case "touch_less":
             washingProgram = new TouchlessCarWashingProgram();
             break;
         default:
             throw new IllegalArgumentException("Unknown washing program type: " + response.getProgramType());
     }
         washingProgram.setId(response.getId());
         washingProgram.setProgramType(response.getProgramType());
         washingProgram.setDescription(response.getDescription());
         washingProgram.setPrice(response.getPrice());
		
		booking.setWashingProgram(washingProgram);


		Booking newBooking = new Booking();
		newBooking.setUser(user); 
		newBooking.setCar(car);  
		newBooking.setStation(station); 
		newBooking.setWashingProgram(washingProgram);
		// Only send notification if user has a valid Firebase device token
		String userDeviceToken = newBooking.getUser().getToken();
		if (userDeviceToken != null && !userDeviceToken.isBlank()) {
		    Message message = Message.builder()
		        .setNotification(Notification.builder()
		            .setTitle("Booking Confirmed 🚗")
		            .setBody("You booked a wash for: " + newBooking.getCar().getRegisterationPlate())
		            .build())
		        .setToken(userDeviceToken) // from mobile app
		        .build();

		    try {
		        String response1 = FirebaseMessaging.getInstance().send(message);
		        System.out.println("✅ Successfully sent message: " + response1);
		    } catch (FirebaseMessagingException e) {
		        System.err.println("❌ Failed to send message: " + e.getMessage());
		    }
		}
			logger.info("The booking for " + booking.getCar().getRegisterationPlate() + " is saved successfully!");
			return new ResponseEntity<>(booking, HttpStatus.CREATED);
	

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