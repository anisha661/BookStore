package com.bookstore.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bookstore.model.User;
import com.bookstore.model.UserPayment;
import com.bookstore.repository.UserPaymentRepository;
import com.bookstore.repository.UserRepository;

@Service
public class UserPaymentServiceImpl implements UserPaymentService {

	@Autowired
	private UserPaymentRepository paymentRepository;
	
	@Override
	public UserPayment findById(Long id) {
	
		return paymentRepository.findById(id).get();
	}

	@Override
	public void removeCreditCard(Long cardId) {
			paymentRepository.deleteById(cardId);
	}

	@Override
	public void setDefaultPayment(Long defaultPaymentId, User user) {
        List<UserPayment> userPaymentList= (List<UserPayment>) paymentRepository.findAll();
        
        for(UserPayment userPayment :userPaymentList) {
        	if(userPayment.getId()== defaultPaymentId) {
        		userPayment.setDefaultPayment(true);
        		paymentRepository.save(userPayment);
        	}else {
        		
        		userPayment.setDefaultPayment(false);
        		paymentRepository.save(userPayment);
        	}
        }
	}

}
