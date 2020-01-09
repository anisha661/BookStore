package com.bookstore.service;

import com.bookstore.model.Payment;
import com.bookstore.model.UserPayment;

public interface PaymentService {

	Payment setByUserPayment(UserPayment userPayment, Payment payment);

}
