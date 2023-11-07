package com.zwash.booking.controller;

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
import com.zwash.auth.exceptions.CarDoesNotExistException;
import com.zwash.auth.exceptions.UserIsNotFoundException;
import com.zwash.booking.config.KafkaTopicConfig;
import com.zwash.booking.dto.BookingDTO;
import com.zwash.booking.exceptions.StationNotExistsException;
import com.zwash.booking.pojos.Booking;
import com.zwash.booking.pojos.CarWashingProgram;
import com.zwash.booking.pojos.Station;
import com.zwash.booking.service.BookingService;
import com.zwash.common.pojos.Car;
import com.zwash.common.pojos.User;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import jakarta.transaction.Transactional;


@Controller
@RequestMapping("v1/bookings")
@Api(tags = "Booking API")
public class BookingController {

	@Autowired
    private KafkaTemplate<String, String> kafkaTemplate;
	@Autowired
	private BookingService bookingService;



	Logger logger = LoggerFactory.getLogger(BookingController.class);

	@GetMapping(value = "/{id}")
	@ApiOperation(value = "Get a booking by ID", response = Booking.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Successfully retrieved booking"),
			@ApiResponse(code = 404, message = "Booking not found") })
	public ResponseEntity<Booking> getBooking(@PathVariable Long id) {
		Booking booking =null;// bookingService.getBookingById(id);
		if (booking != null) {
			return new ResponseEntity<>(booking, HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}

	@GetMapping
	@ApiOperation(value = "Get all bookings", response = BookingDTO.class, responseContainer = "List")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Successfully retrieved bookings"),
			@ApiResponse(code = 404, message = "Bookings not found") })
	public ResponseEntity<List<BookingDTO>> getAllBookings() throws Exception {
		try {
			List<BookingDTO> list =null;//  bookingService.getAllBookings();
			return new ResponseEntity<>(list, HttpStatus.OK);
		} catch (Exception ex) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}

	@GetMapping("/user/{id}")
	@ApiOperation(value = "Get bookings belong to a User", response = BookingDTO.class, responseContainer = "List")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Successfully retrieved bookings"),
			@ApiResponse(code = 404, message = "Bookings not found") })
	public ResponseEntity<List<BookingDTO>> getUsersBookings(@PathVariable("id") Long userId,
            @RequestBody(required = false) String reqJson) throws Exception {
		try {
			 kafkaTemplate.send(KafkaTopicConfig.getUserTopic().name(), userId.toString());
			 
			 CompletableFuture<User> userFuture = new CompletableFuture<>();
			   // Set up a callback to handle the asynchronous user retrieval
			    userFuture.thenApplyAsync(user -> {
			        try {
			            // If the user is null, handle the scenario (throw exception, return appropriate response, etc.)
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
	@ApiOperation(value = "Create a booking", response = Booking.class)
	@ApiResponses(value = { @ApiResponse(code = 201, message = "Successfully created booking"),
			@ApiResponse(code = 400, message = "Invalid request") })
	public ResponseEntity<Booking> createBooking(@RequestBody BookingDTO bookingDto) throws  CarDoesNotExistException, StationNotExistsException, UserIsNotFoundException {

		Booking booking = new Booking();

		if (bookingDto == null) {
			throw new IllegalArgumentException("Booking  cannot be null");
		}

		if(bookingDto.getToken()!=null)
		{
			booking.setToken(bookingDto.getToken());
		}
		User user = null;// userService.getUserFromToken(bookingDto.getToken());

		booking.setUser(user);

		if (bookingDto.getStationId() == null) {
			throw new IllegalArgumentException("Station object cannot be null");
		}

		Station station=null;//  stationService.getStation(bookingDto.getStationId());
		booking.setStation(station);

		if (bookingDto.getCarId() == null) {
			throw new IllegalArgumentException("Car object cannot be null");
		}
		Car car = null;// carService.getCar(bookingDto.getCarId());

		if (car == null) {
			logger.error("The car " + booking.getCar().getRegisterationPlate() + "  is not registered in the system!");
			throw new IllegalArgumentException("There is no car in the system with registeration number "
					+ booking.getCar().getRegisterationPlate());
		}

		booking.setCar(car);


		if (bookingDto.getWashingProgramId() == null) {
			throw new IllegalArgumentException("Washing program object cannot be null");
		}
		CarWashingProgram washingProgram = null;// washingProgramService.getProgramById(bookingDto.getWashingProgramId());

       booking.setWashingProgram(washingProgram);

    	Booking newBooking = null;// bookingService.saveBooking(booking);
		if (newBooking instanceof Booking) {
			// Construct the message
//			Message message = Message.builder()
//					.setNotification(Notification.builder().setTitle("Booking made!")
//							.setBody("You have made a booking for car: " + booking.getCar().getRegisterationPlate())
//							.build())
//					.setToken(booking.getUser().getToken())// to get from the react native app later device token
//					.build();

			// Send the message
//			try {
//				String response = FirebaseMessaging.getInstance().send(message);
//				System.out.println("Successfully sent message: " + response);
//			} catch (FirebaseMessagingException e) {
//				System.out.println("Failed to send message: " + e.getMessage());
//			}
			logger.info("The booking for " + booking.getCar().getRegisterationPlate() + " is saved successfully!");
			return new ResponseEntity<>(booking, HttpStatus.CREATED);
		} else {
			return new ResponseEntity<>(booking, HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

	@PutMapping("/{id}")
	@Transactional
	@ApiOperation(value = "Update an existing booking", response = Booking.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Booking updated successfully"),
			@ApiResponse(code = 400, message = "Invalid request. Check input parameters"),
			@ApiResponse(code = 404, message = "Booking with provided id not found") })
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


		Booking booking =null;//  bookingService.getBookingById(id);
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
	@ApiOperation(value = "Execute a Wash")
	@ApiResponses(value = { @ApiResponse(code = 204, message = "Wash executed successfully"),
			@ApiResponse(code = 404, message = "Booking with provided id not found") })
	public ResponseEntity<Void> executeBookingWash(@PathVariable Long id) {
		Booking booking = null;// bookingService.getBookingById(id);
		if (booking != null) {
			//carWashService.executeCarWash(booking);

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
	@ApiOperation(value = "Delete a booking by id")
	@ApiResponses(value = { @ApiResponse(code = 204, message = "Booking deleted successfully"),
			@ApiResponse(code = 404, message = "Booking with provided id not found") })
	public ResponseEntity<Void> deleteBooking(@PathVariable Long id) {
		Booking booking = null;// bookingService.getBookingById(id);
		if (booking != null) {
			logger.info(
					"the  booking for " + booking.getCar().getRegisterationPlate() + " has been deleted successfully");

			//bookingService.deleteBooking(booking);
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		} else {
			logger.error("Booking with car " + booking.getCar().getRegisterationPlate() + " not deleted!");

			throw new IllegalArgumentException("Booking with id " + id + " not found");
		}
	}

	@GetMapping("validate/{registrationPlate}")
	@ResponseStatus(HttpStatus.OK)
	@ApiOperation(value = "Check if a booking exists for a given car registration plate", response = Boolean.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Request processed successfully"),
			@ApiResponse(code = 404, message = "Car with provided registration plate not found") })
	public ResponseEntity<Boolean> isBookingExistsForCar(@PathVariable String registrationPlate) throws CarDoesNotExistException {
		//Car car =null;
		//carService.getCar(registrationPlate);
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

}