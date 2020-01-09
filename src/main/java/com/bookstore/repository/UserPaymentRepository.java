package com.bookstore.repository;

import org.springframework.data.repository.CrudRepository;

import com.bookstore.model.UserPayment;

public interface UserPaymentRepository extends CrudRepository<UserPayment, Long> {

}
