package com.zwash.booking.pojos;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ConcreteCarWashingProgram {

	 @JsonProperty(value = "id")
	   private int id;

	   @JsonProperty(value = "price")
	    private long price;

	   @JsonProperty(value = "description")
	    private String description;

	   @JsonProperty(value = "program")
	    private String program;

	   @JsonProperty("paymentMethodId")
	    private String paymentMethodId;

	@JsonProperty(value = "updatedAt")
	   private List<Integer> updatedAt;
	   @JsonProperty(value = "createdAt")
	   private List<Integer> createdAt;

	   public List<Integer> getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(List<Integer> updatedAt) {
		this.updatedAt = updatedAt;
	}

	public List<Integer> getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(List<Integer> createdAt) {
		this.createdAt = createdAt;
	}
    public long getPrice() {
        return price;
    }

    public void setPrice(long price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getProgram() {
        return program;
    }

    public void setProgram(String program) {
        this.program = program;
    }

    public String getPaymentMethodId() {
        return paymentMethodId;
    }

    public void setPaymentMethodId(String paymentMethodId) {
        this.paymentMethodId = paymentMethodId;
    }
}

