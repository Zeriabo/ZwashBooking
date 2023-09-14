package com.zwash.booking.pojos;

import java.time.LocalDateTime;
import java.util.List;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;

@Entity
@Table(name = "station")
public class Station {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false)
	private String name;

	@Column(nullable = false)
	private String address;

	@OneToOne
	@JoinColumn(name = "media_id")
	private Media media;

	@Column
	private double latitude;

	@Column
	private double longitude;

	@JsonManagedReference
	@OneToMany(mappedBy = "station", cascade = CascadeType.ALL)
	@JsonIgnore
	private List<CarWashingProgram> programs;

	@ManyToOne
	@JoinColumn(name = "service_provider_id")
	private ServiceProvider serviceProvider;

	@CreationTimestamp
	@Column(name = "createdAt")
	private LocalDateTime createdAt;

	@UpdateTimestamp
	@Column(name = "updatedAt")
	private LocalDateTime updatedAt;
	
	public Station() {
	}
	
	public Station(String name, String address, double latitude, double longitude, ServiceProvider serviceProvider,
			Media media) {

		this.name = name;
		this.address = address;
		this.latitude = latitude;
		this.longitude = longitude;
		this.serviceProvider = serviceProvider;
		this.media = media;

	}


	public Station(String name, String address, double latitude, double longitude, ServiceProvider serviceProvider) {
		
		this.name = name;
		this.address = address;
		this.latitude = latitude;
		this.longitude = longitude;
		this.serviceProvider = serviceProvider;

	}

	public Media getMedia() {
		return media;
	}

	public void setMedia(Media media) {
		this.media = media;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public double getLatitude() {
		return latitude;
	}

	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	public double getLongitude() {
		return longitude;
	}

	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}

	public List<CarWashingProgram> getPrograms() {
		return programs;
	}

	public void setPrograms(List<CarWashingProgram> programs) {
		this.programs = programs;
	}

	public ServiceProvider getServiceProvider() {
		return serviceProvider;
	}

	public void setServiceProvider(ServiceProvider serviceProvider) {
		this.serviceProvider = serviceProvider;
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
