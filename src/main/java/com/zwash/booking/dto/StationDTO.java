package com.zwash.booking.dto;

import java.util.List;



public class StationDTO {

    private String name;
    private String address;
    private double latitude;
    private double longitude;
    private List<CarWashingProgramDTO> programs;
    // Default constructor
    public StationDTO() {
    }


    public StationDTO(String name, double latitude, double longitude) {
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
    }
    public StationDTO(String name,String address, double latitude, double longitude ) {
        this.name = name;
        this.address=address;
        this.latitude = latitude;
        this.longitude = longitude;

    }
    public StationDTO(String name, double latitude, double longitude, List<CarWashingProgramDTO> programs ) {
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
        this.programs=programs;
    }

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
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
	public  List<CarWashingProgramDTO> getPrograms() {
		return programs;
	}
	public void setPrograms(List<CarWashingProgramDTO> programs) {
		this.programs = programs;
	}


	public String getAddress() {
		return address;
	}


	public void setAddress(String address) {
		this.address = address;
	}


}

