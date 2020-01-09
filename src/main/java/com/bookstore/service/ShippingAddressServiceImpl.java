package com.bookstore.service;

import org.springframework.stereotype.Service;

import com.bookstore.model.ShippingAddress;
import com.bookstore.model.UserShipping;

@Service
public class ShippingAddressServiceImpl implements ShippingAddressService {

	@Override
	public ShippingAddress setByUserShipping(UserShipping userShipping, ShippingAddress shippingAddress) {
		
		shippingAddress.setShippingAddressName(userShipping.getShippingName());
		shippingAddress.setShippingAddressStreet(userShipping.getShippingStreet());
		shippingAddress.setShippingAddressCity(shippingAddress.getShippingAddressCity());
		shippingAddress.setShippingAddressCountry(userShipping.getShippingCountry());
		
		return shippingAddress;
	}

}
