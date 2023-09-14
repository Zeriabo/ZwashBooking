package com.zwash.booking.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.zwash.auth.pojos.User;
import com.zwash.booking.pojos.Booking;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {

	 @Query("SELECT b FROM Booking b WHERE b.car.id = :carId")
	 List<Booking> findByCarId(@Param("carId") Long carId);

	 @Query("SELECT b FROM Booking b WHERE b.id = :id and b.executed=false")
	  Optional<Booking> findByIdAndExecutedFalse(Long id);

	 @Query("SELECT b FROM Booking b WHERE b.car.id = :carId and b.executed=:executed")
	  Booking findByCarAndExecuted(Long carId, boolean executed);

	 @Query("SELECT b FROM Booking b WHERE b.user = :user")
	 List<Booking> findByUser(User user);

	 @Modifying
	 @Query("Update  Booking b SET b.executed=true WHERE b.id = :id")
	 void executeWash(@Param("id") Long id);

	 @Query("SELECT b FROM Booking b WHERE b.user.id = :userId")
	List<Booking> findByUserId(@Param("userId") Long userId);


}

