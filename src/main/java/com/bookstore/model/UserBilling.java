package com.bookstore.model;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;

@Entity
public class UserBilling {

	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String billingName;
	private String billingStreet;
	private String billingCity;
	private String billingCountry;
	
	
	@OneToOne(cascade = CascadeType.ALL)
	private UserPayment userPayment;


	public Long getId() {
		return id;
	}


	public void setId(Long id) {
		this.id = id;
	}


	public String getBillingName() {
		return billingName;
	}


	public void setBillingName(String billingName) {
		this.billingName = billingName;
	}


	public String getBillingStreet() {
		return billingStreet;
	}


	public void setBillingStreet(String billingStreet) {
		this.billingStreet = billingStreet;
	}


	public String getBillingCity() {
		return billingCity;
	}


	public void setBillingCity(String billingCity) {
		this.billingCity = billingCity;
	}


	public String getBillingCountry() {
		return billingCountry;
	}


	public void setBillingCountry(String billingCountry) {
		this.billingCountry = billingCountry;
	}


	public UserPayment getUserPayment() {
		return userPayment;
	}


	public void setUserPayment(UserPayment userPayment) {
		this.userPayment = userPayment;
	}
	
	
}
