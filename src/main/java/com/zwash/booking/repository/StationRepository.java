package com.zwash.booking.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.zwash.booking.pojos.CarWashingProgram;
import com.zwash.booking.pojos.Media;
import com.zwash.booking.pojos.Station;


@Repository
public interface StationRepository extends JpaRepository<Station, Long> {

	@Override
	@Query("SELECT s FROM Station s WHERE s.id = :id")
	Optional<Station> findById(@Param("id") Long id);

	@Query("SELECT s FROM Station  s WHERE s.serviceProvider.id = :id")
	List<Station> findByServiceProvider(@Param("id") Long id);

	@Query("""
            SELECT cw \
            FROM CarWashingProgram cw \
            WHERE cw.station.id =:id\
            """)
	List<CarWashingProgram> getWashes(@Param("id") Long id);

	@Modifying
	@Query("UPDATE Station s SET s.media = :media WHERE s.id = :id")
	void setMedia(@Param("id") Long id, @Param("media") Media media);

	@Modifying
	@Query("UPDATE Station s SET s.latitude = :latitude, s.longitude = :longitude WHERE s.id = :id")
	void setAddress(@Param("id") Long id, @Param("latitude") Long latitude, @Param("longitude") Long longitude);


	@Query("SELECT s FROM Station s WHERE s.serviceProvider.id = :id")
	List<Station> selectAllStationsByServiceProvider(@Param("id") Long id);
}