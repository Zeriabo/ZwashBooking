package com.zwash.booking.factory;

import com.zwash.booking.pojos.CarWashingProgram;
import com.zwash.booking.pojos.FoamCarWashingProgram;
import com.zwash.booking.pojos.HighPressureCarWashingProgram;
import com.zwash.booking.pojos.TouchlessCarWashingProgram;

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
