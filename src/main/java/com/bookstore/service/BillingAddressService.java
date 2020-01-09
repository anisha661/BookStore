package com.bookstore.service;

import com.bookstore.model.BillingAddress;
import com.bookstore.model.UserBilling;

public interface BillingAddressService {
	
	BillingAddress setByUserBilling(UserBilling userBilling,BillingAddress billingAddress);

}
