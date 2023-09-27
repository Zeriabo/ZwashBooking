package com.zwash.booking.dto;

public class CarWashingProgramDTO {

    private String name;
    private int duration;
    private String programType;
    double price;

    public CarWashingProgramDTO()
    {

    }
    public CarWashingProgramDTO(String name, int duration, String programType, double price) {
        this.name = name;
        this.duration = duration;
        this.programType = programType;
        this.price=price;
    }
    // Getters and setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public String getProgramType() {
        return programType;
    }

    public void setProgramType(String programType) {
        this.programType = programType;
    }


}
