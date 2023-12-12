package com.zwash.booking.pojos;

import java.time.LocalDateTime;


import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.zwash.common.pojos.Car;
import com.zwash.common.pojos.User;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;

@Entity
@Table(name = "bookings")
@Inheritance(strategy = InheritanceType.JOINED)

public class Booking {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne
	@JoinColumn(name = "car_id", nullable = false)
	private Car car;

	@ManyToOne(fetch = FetchType.LAZY, optional = false, cascade = CascadeType.PERSIST)
	@JoinColumn(name = "washing_program_id", nullable = false)

	@JsonSubTypes({ @JsonSubTypes.Type(value = FoamCarWashingProgram.class, name = "foam"),
			@JsonSubTypes.Type(value = HighPressureCarWashingProgram.class, name = "high_pressure"),
			@JsonSubTypes.Type(value = TouchlessCarWashingProgram.class, name = "touch_less") })
	private CarWashingProgram washingProgram;

	//i dont need the sceduled time
	@ManyToOne(fetch = FetchType.LAZY, optional = false, cascade = CascadeType.PERSIST)
	@JoinColumn(name = "user_id", nullable = false)
	private User user;


	@ManyToOne
	@JoinColumn(name = "station_id", nullable = false)
	private Station station;

	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "wash_id", referencedColumnName = "id")
	private Wash wash;

	private String token;

	private boolean executed;

	private LocalDateTime createdAt;

	private LocalDateTime updatedAt;

	public Booking() {
	}

	public Booking(Car car, Station station,CarWashingProgram washingProgram,User user) {
		this.car = car;
		this.washingProgram = washingProgram;
		this.user=user;
		this.station=station;

	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Car getCar() {
		return car;
	}

	public void setCar(Car car) {
		this.car = car;
	}

	public CarWashingProgram getWashingProgram() {
		return washingProgram;
	}

	public void setWashingProgram(CarWashingProgram washingProgram) {
		this.washingProgram = washingProgram;
	}


	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public Station getStation() {
		return station;
	}

	public void setStation(Station station) {
		this.station = station;
	}

	public boolean isExecuted() {
		return executed;
	}

	public void setExecuted(boolean executed) {
		this.executed = executed;
	}

	public Wash getWash() {
		return wash;
	}

	public void setWash(Wash wash) {
		this.wash = wash;
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}

	public LocalDateTime getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(LocalDateTime updatedAt) {
		this.updatedAt = updatedAt;
	}

	@PrePersist
	protected void onCreate() {
		setCreatedAt(LocalDateTime.now());
		setUpdatedAt(LocalDateTime.now());
	}

	@PreUpdate
	protected void onUpdate() {
		setUpdatedAt(LocalDateTime.now());
	}

}
