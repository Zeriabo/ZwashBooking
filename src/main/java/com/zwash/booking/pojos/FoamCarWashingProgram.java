package com.zwash.booking.pojos;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
@DiscriminatorValue("foam")
public class FoamCarWashingProgram extends CarWashingProgram {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	private int waterPressure;
	private int soapAmount;
	private String brushType;

	public FoamCarWashingProgram() {
		// Empty constructor required for JPA
	}

	public FoamCarWashingProgram(int waterPressure, int soapAmount, String brushType) {
		this.waterPressure = waterPressure;
		this.soapAmount = soapAmount;
		this.brushType = brushType;
	}

	// Getters and setters

	@Override
	public void setWaterPressure(int waterPressure) {
		this.waterPressure = waterPressure;
	}

	@Override
	public void setSoapAmount(int soapAmount) {
		this.soapAmount = soapAmount;
	}

	@Override
	public void setBrushType(String brushType) {
		this.brushType = brushType;
	}

	@Override
	public void startWashing() {
		// Implementation details for starting a foam car washing program
		System.out.println("Starting foam car washing program with " + waterPressure + " water pressure, " + soapAmount
				+ " soap amount, and " + brushType + " brush type.");
	}



}
