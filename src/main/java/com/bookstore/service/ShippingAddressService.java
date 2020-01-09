package com.bookstore.service;

import com.bookstore.model.ShippingAddress;
import com.bookstore.model.UserShipping;

public interface ShippingAddressService {

	public ShippingAddress setByUserShipping(UserShipping userShipping, ShippingAddress shippingAddress);
	

}
