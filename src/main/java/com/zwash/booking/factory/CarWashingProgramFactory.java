package com.zwash.booking.factory;


import com.zwash.common.pojos.CarWashingProgram;
import com.zwash.common.pojos.FoamCarWashingProgram;
import com.zwash.common.pojos.HighPressureCarWashingProgram;
import com.zwash.common.pojos.TouchlessCarWashingProgram;

public abstract class CarWashingProgramFactory {

    public static CarWashingProgram createCarWashingProgram(String dtype) {
        switch (dtype) {
            case "foam":
                return new FoamCarWashingProgram();
            case "high_pressure":
                return new HighPressureCarWashingProgram();
            case "touch_less":
                return new TouchlessCarWashingProgram();
            default:
                throw new IllegalArgumentException("Invalid washing program : " + dtype);
        }
    }
}
