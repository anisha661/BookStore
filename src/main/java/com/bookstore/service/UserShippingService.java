package com.bookstore.service;

import com.bookstore.model.User;
import com.bookstore.model.UserShipping;

public interface UserShippingService {
	
	UserShipping findById(Long id);

	void setDefaultShippingAddress(Long defaultShippingAddressId, User user);

	void removeShippingAddress(Long shippingId);

	

}
