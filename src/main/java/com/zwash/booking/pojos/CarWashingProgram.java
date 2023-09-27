package com.zwash.booking.pojos;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;

@Entity
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.EXISTING_PROPERTY, property = "programType")
@JsonSubTypes({ @JsonSubTypes.Type(value = HighPressureCarWashingProgram.class, name = "high_pressure"),
		@JsonSubTypes.Type(value = FoamCarWashingProgram.class, name = "foam"),
		@JsonSubTypes.Type(value = TouchlessCarWashingProgram.class, name = "touch_less") })
@JsonIgnoreProperties(ignoreUnknown = true)
public abstract class CarWashingProgram {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne
	private Station station;


	@JsonProperty("program")
	@Column(name = "program_type")
	private String program;

	@Column(name = "description")
	private String description;

	@Column(name = "price")
	private double price;

	@CreationTimestamp
	@Column(name = "createdAt")
	private LocalDateTime createdAt;

	@UpdateTimestamp
	@Column(name = "updatedAt")
	private LocalDateTime updatedAt;

	public abstract void setWaterPressure(int pressure);

	public abstract void setSoapAmount(int amount);

	public abstract void setBrushType(String brushType);

	public abstract void startWashing();


	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getProgramType() {
		return program;
	}

	public void setProgramType(String program) {
		this.program = program;
	}

	public Station getStation() {
		return station;
	}

	public void setStation(Station station) {
		this.station = station;
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

	public String getDescription()
	{
		return description;
	}

	public void setDescription(String description)
	{
		 this.description=description;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
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