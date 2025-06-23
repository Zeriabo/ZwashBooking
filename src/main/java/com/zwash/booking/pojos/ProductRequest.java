package com.zwash.booking.pojos;


public class ProductRequest {
    private String name;
    private int price;
	private String description;

    public String getDescription() {
 		return description;
 	}
 	public void setDescription(String description) {
 		this.description = description;
 	}
 	public int getPrice() {
 		return price;
 	}
 	public void setPrice(int price) {
 		this.price = price;
 	}

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}

}