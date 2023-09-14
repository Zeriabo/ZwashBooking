package com.zwash.booking.pojos;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
@DiscriminatorValue("high_pressure")
public class HighPressureCarWashingProgram extends CarWashingProgram {



    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private int waterPressure;


    public HighPressureCarWashingProgram() {
        // Empty constructor required for JPA
    }

    public HighPressureCarWashingProgram(int waterPressure) {
        this.waterPressure = waterPressure;
    }

    // Getters and setters

    @Override
    public void setWaterPressure(int waterPressure) {
        this.waterPressure = waterPressure;
    }

    @Override
    public void setSoapAmount(int soapAmount) {
        // This method doesn't apply to high-pressure car washing programs
    }

    @Override
    public void setBrushType(String brushType) {
        // This method doesn't apply to high-pressure car washing programs
    }

    @Override
    public void startWashing() {
        // Implementation details for starting a high-pressure car washing program
        System.out.println("Starting high-pressure car washing program with " + waterPressure + " water pressure.");
    }


}
