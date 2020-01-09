package com.bookstore.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.bookstore.model.User;
import com.bookstore.model.UserPayment;
import com.bookstore.model.UserShipping;
import com.bookstore.repository.UserShippingRepository;

@Repository
public class UserShippingServiceImpl implements UserShippingService {

	@Autowired
	private UserShippingRepository shippingRepository;
	
	@Override
	public UserShipping findById(Long id) {
		return shippingRepository.findById(id).get();
	}

	@Override
	public void setDefaultShippingAddress(Long defaultShippingAddressId, User user) {
		 List<UserShipping> userShippingList= (List<UserShipping>) shippingRepository.findAll();
	        
	        for(UserShipping userShipping :userShippingList) {
	        	if(userShipping.getId()== defaultShippingAddressId) {
	        		userShipping.setShippingDefault(true);
	        		shippingRepository.save(userShipping);
	        	}else {
	        		
	        		userShipping.setShippingDefault(false);
	        		shippingRepository.save(userShipping);
	        	}
	        }
		
	}

	@Override
	public void removeShippingAddress(Long shippingId) {

		shippingRepository.deleteById(shippingId);
	}

}
