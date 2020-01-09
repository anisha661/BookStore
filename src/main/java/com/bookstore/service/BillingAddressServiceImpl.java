package com.bookstore.service;

import org.springframework.stereotype.Service;

import com.bookstore.model.BillingAddress;
import com.bookstore.model.UserBilling;

@Service
public class BillingAddressServiceImpl implements BillingAddressService {

	@Override
	public BillingAddress setByUserBilling(UserBilling userBilling, BillingAddress billingAddress) {
	
		billingAddress.setBillingAddressName(userBilling.getBillingName());
		billingAddress.setBillingAddressStreet(userBilling.getBillingStreet());	
		billingAddress.setBillingAddressCity(userBilling.getBillingCity());
		billingAddress.setBillingAddressCountry(userBilling.getBillingCountry());
		return billingAddress;
	}

}
