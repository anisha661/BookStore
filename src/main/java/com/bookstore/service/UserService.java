package com.bookstore.service;

import com.bookstore.model.PasswordResetToken;
import com.bookstore.model.User;
import com.bookstore.model.UserBilling;
import com.bookstore.model.UserPayment;
import com.bookstore.model.UserShipping;

public interface UserService {
	
	void saveUser(User user);
	
	User findbyUsername(String username);
	
	
	
	User findByEmail(String email);
    PasswordResetToken getPasswordResetToken(final String token);
	
	void createPasswordResetTokenForUser(final User user, final String token);

	void updateUserBilling(UserBilling userBilling, UserPayment userPayment, User user);

	void updateUserShipping(UserShipping userShipping, User user);

	User findById(Long id);


}
