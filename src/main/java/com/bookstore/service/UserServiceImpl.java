package com.bookstore.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bookstore.model.PasswordResetToken;
import com.bookstore.model.User;
import com.bookstore.model.UserBilling;
import com.bookstore.model.UserPayment;
import com.bookstore.model.UserShipping;
import com.bookstore.repository.PasswordResetTokenRepository;
import com.bookstore.repository.UserRepository;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private PasswordResetTokenRepository tokenRepository;

	@Override
	public User findbyUsername(String username) {
		return userRepository.findByUsername(username);
	}


	@Override
	public void saveUser(User user) {
		userRepository.save(user);
	}

	@Override
	public User findByEmail(String email) {
		return userRepository.findByEmail(email);
	}


	@Override
	public PasswordResetToken getPasswordResetToken(String token) {
		
		return tokenRepository.findByToken(token);
	}


	@Override
	public void createPasswordResetTokenForUser(User user, String token) {
		PasswordResetToken myToken = new PasswordResetToken(token,user);
		tokenRepository.save(myToken);
	}


	@Override
	public void updateUserBilling(UserBilling userBilling, UserPayment userPayment, User user) {
		userPayment.setUser(user);
		userPayment.setUserBilling(userBilling);
		userPayment.setDefaultPayment(true);
		userBilling.setUserPayment(userPayment);
		user.getUserPaymentList().add(userPayment);
		saveUser(user);
		
		
	}


	@Override
	public void updateUserShipping(UserShipping userShipping, User user) {

		userShipping.setUser(user);
		userShipping.setShippingDefault(true);
		user.getUserShippingList().add(userShipping);
		saveUser(user);
		
		
	}


	@Override
	public User findById(Long id) {
		
		return userRepository.findById(id).get();
	}

	

	
}
