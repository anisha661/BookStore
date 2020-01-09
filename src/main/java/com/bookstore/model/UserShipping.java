package com.bookstore.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class UserShipping {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String shippingName;
	private String shippingStreet;
	private String shippingCity;
	private String shippingCountry;
	private boolean shippingDefault;
	
	
	
	@ManyToOne
	@JoinColumn(name="user_id")
	private User user;


	public Long getId() {
		return id;
	}


	public void setId(Long id) {
		this.id = id;
	}


	public String getShippingName() {
		return shippingName;
	}


	public void setShippingName(String shippingName) {
		this.shippingName = shippingName;
	}


	public String getShippingStreet() {
		return shippingStreet;
	}


	public void setShippingStreet(String shippingStreet) {
		this.shippingStreet = shippingStreet;
	}


	public String getShippingCity() {
		return shippingCity;
	}


	public void setShippingCity(String shippingCity) {
		this.shippingCity = shippingCity;
	}


	public String getShippingCountry() {
		return shippingCountry;
	}


	public void setShippingCountry(String shippingCountry) {
		this.shippingCountry = shippingCountry;
	}


	public User getUser() {
		return user;
	}


	public void setUser(User user) {
		this.user = user;
	}


	public boolean isShippingDefault() {
		return shippingDefault;
	}


	public void setShippingDefault(boolean shippingDefault) {
		this.shippingDefault = shippingDefault;
	}
	
	
	
}
