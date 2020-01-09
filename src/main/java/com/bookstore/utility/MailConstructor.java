package com.bookstore.utility;

import java.util.Locale;

import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import com.bookstore.model.Order;
import com.bookstore.model.User;


@Component
public class MailConstructor {
	
	@Autowired
	private TemplateEngine templateEngine;
	
	public SimpleMailMessage constructResetTokenEmail(String contextPath, Locale locale, String token, User user) {
		
		String url = contextPath + "/confirm?token="+token;
		String message = "\nPlease click on this link to verify your email and edit your personal information \n";
		SimpleMailMessage email = new SimpleMailMessage();
		email.setTo(user.getEmail());
		email.setSubject("Registration confirmation");
		email.setText(message + url);
		email.setFrom("binnie616@gmail.com");
		return email;
	}

	public MimeMessagePreparator constructOrderConfirmationEmail(User user, Order order, Locale english) {
		
		Context context = new Context();
		context.setVariable("order",order);
		context.setVariable("user", user);
		context.setVariable("cartItemList", order.getCartItemList());
		String text = templateEngine.process("orderConfirmationEmail", context);
		
		MimeMessagePreparator message=new MimeMessagePreparator() {
			
			@Override
			public void prepare(MimeMessage mimeMessage) throws Exception {

				MimeMessageHelper email = new MimeMessageHelper(mimeMessage);
				email.setTo(user.getEmail());
				email.setSubject("Order Confirmation :"+order.getId());
				email.setText(text,true);
				email.setFrom(new InternetAddress("binnie616@gmail.com"));
				
			}
		};
		
		return message;
		
	}
}
