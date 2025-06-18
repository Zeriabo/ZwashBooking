package com.zwash.booking.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.zwash.common.pojos.Wash;

@Repository
public interface WashRepository extends JpaRepository<Wash, Long> {

	@Query("SELECT w FROM Wash w WHERE w.booking.id = :bookingId")
	Wash findByBookingId(Long bookingId);

}


