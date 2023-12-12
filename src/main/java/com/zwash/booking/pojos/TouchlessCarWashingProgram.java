package com.zwash.booking.pojos;


import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
@DiscriminatorValue("touch_less")
public class TouchlessCarWashingProgram extends CarWashingProgram {

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
    private int waterPressure;
    private int soapAmount;

    public TouchlessCarWashingProgram(){

    }
    public TouchlessCarWashingProgram(int waterPressure, int soapAmount) {
        this.waterPressure = waterPressure;
        this.soapAmount = soapAmount;
    }


    @Override
    public void setWaterPressure(int pressure) {
        this.waterPressure = pressure;
    }

    @Override
    public void setSoapAmount(int amount) {
        this.soapAmount = amount;
    }

    @Override
    public void setBrushType(String brushType) {

    }

    @Override
    public void startWashing() {
        System.out.println("Starting touchless car washing program with " + waterPressure + " water pressure and " + soapAmount + " soap amount.");
    }

}
