package com.bookstore.service;

import com.bookstore.model.User;
import com.bookstore.model.UserPayment;

public interface UserPaymentService {
	
	UserPayment findById(Long id);

	void removeCreditCard(Long cardId);

	void setDefaultPayment(Long defaultPaymentId, User user);

	

}
