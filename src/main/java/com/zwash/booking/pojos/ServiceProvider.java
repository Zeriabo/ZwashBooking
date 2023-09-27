package com.zwash.booking.pojos;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "service_provider")
public class ServiceProvider {

	public ServiceProvider()
	{

	}
	public ServiceProvider(Long id) {
		this.id=id;
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

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public List<Station> getStations() {
		return stations;
	}

	public void setStations(List<Station> stations) {
		this.stations = stations;
	}

	public ServiceProviderUser getServiceProviderUser() {
		return serviceProviderUser;
	}

	public void setServiceProviderUser(ServiceProviderUser serviceProviderUser) {
		this.serviceProviderUser = serviceProviderUser;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false)
	private String name;

	@Column(nullable = false)
	private String email;

	@ManyToOne
	private ServiceProviderUser serviceProviderUser;



	@OneToMany(mappedBy = "serviceProvider", cascade = CascadeType.ALL)
	@JsonIgnore
	private List<Station> stations;

}