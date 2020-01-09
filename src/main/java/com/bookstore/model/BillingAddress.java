package com.bookstore.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;

@Entity
public class BillingAddress {

	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String billingAddressName;
	private String billingAddressStreet;
	private String billingAddressCity;
	private String billingAddressCountry;
	
	
	@OneToOne
	private Order order;


	public Long getId() {
		return id;
	}


	public void setId(Long id) {
		this.id = id;
	}


	public String getBillingAddressName() {
		return billingAddressName;
	}


	public void setBillingAddressName(String billingAddressName) {
		this.billingAddressName = billingAddressName;
	}


	public String getBillingAddressStreet() {
		return billingAddressStreet;
	}


	public void setBillingAddressStreet(String billingAddressStreet) {
		this.billingAddressStreet = billingAddressStreet;
	}


	public String getBillingAddressCity() {
		return billingAddressCity;
	}


	public void setBillingAddressCity(String billingAddressCity) {
		this.billingAddressCity = billingAddressCity;
	}


	public String getBillingAddressCountry() {
		return billingAddressCountry;
	}


	public void setBillingAddressCountry(String billingAddressCountry) {
		this.billingAddressCountry = billingAddressCountry;
	}


	public Order getOrder() {
		return order;
	}


	public void setOrder(Order order) {
		this.order = order;
	}


	
	
	
}
