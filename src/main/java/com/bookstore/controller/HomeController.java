package com.bookstore.controller;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.bookstore.model.PasswordResetToken;
import com.bookstore.model.Role;
import com.bookstore.model.ShoppingCart;
import com.bookstore.model.User;
import com.bookstore.model.UserPayment;
import com.bookstore.model.UserShipping;
import com.bookstore.repository.RoleRepository;
import com.bookstore.service.UserDetailsServiceImpl;
import com.bookstore.service.UserService;
import com.bookstore.utility.MailConstructor;

@Controller
public class HomeController {
	
	
	@Autowired
	private UserDetailsServiceImpl service;
	@Autowired
	private BCryptPasswordEncoder encoder;
	@Autowired
	private RoleRepository roleRepository;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private MailConstructor mailConstructor;
	
	@Autowired
	private JavaMailSender mailSender;
	
	@RequestMapping("/login")
	public String loginUser(Model m ,String error ,String logout) {
	if(error!=null) {
		m.addAttribute("error", true);
	}
	if(logout!=null)
		m.addAttribute("msg", true);
		return "login";
	}
	
	
	@RequestMapping(value="/login" ,method=RequestMethod.POST)
	public String returnLogin() {
		return "login";
	
	}
	
	@RequestMapping(value="/signup" ,method=RequestMethod.GET)
	public String returnSignUp(Model m ) {
		User user = new User();
		m.addAttribute("user", user);
		return "registration";
	
	}
	
	
	
	@RequestMapping(value = "/signup" ,method = RequestMethod.POST)
	public String signup(@ModelAttribute("user") User user , Model m ,HttpServletRequest request) {
		
		User usernameExists =userService.findbyUsername(user.getUsername());
		User emailExists=userService.findByEmail(user.getEmail());
		
		if(usernameExists != null) {
			m.addAttribute("userExists", true);
			return "registration";
		}
		
		if(emailExists != null) {
			m.addAttribute("emailExists", true);
			return "registration";
		}
		
		user.setPassword(encoder.encode(user.getPassword()));
		
		user.setEnabled(false);
		
		
		 Set<Role> roles=new HashSet<>();
	     roles.add(roleRepository.getOne(2L));
	    user.setRoles(roles);
	    
	    ShoppingCart shoppingCart= new ShoppingCart();
	    shoppingCart.setUser(user);
	    user.setShoppingCart(shoppingCart);
	    
	    user.setUserShippingList(new ArrayList<UserShipping>());
	    user.setUserPaymentList(new ArrayList<UserPayment>());
	    
	    userService.saveUser(user);
		
		
		String token = UUID.randomUUID().toString();
		userService.createPasswordResetTokenForUser(user, token);
		
		String appUrl = "http://"+request.getServerName()+":"+request.getServerPort()+request.getContextPath();
		
		SimpleMailMessage email = mailConstructor.constructResetTokenEmail(appUrl, request.getLocale(), token, user);
		
		mailSender.send(email);
		
		
		m.addAttribute("emailSent","A confirmation email has been sent to "+user.getEmail());
		
		return "registration";
		
	}
	
	@RequestMapping("/confirm")
	public String processSignup(Locale locale,@RequestParam("token") String token, Model m) {
		PasswordResetToken passToken = userService.getPasswordResetToken(token);

		if (passToken == null) {
			String message = "Invalid Token.";
			m.addAttribute("message", message);
			return "redirect:/badRequest";
		}
		

		User user = passToken.getUser();
		String username = user.getUsername();

		UserDetails userDetails = service.loadUserByUsername(username);

		Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, userDetails.getPassword(),
				userDetails.getAuthorities());
		
		SecurityContextHolder.getContext().setAuthentication(authentication);
		
		m.addAttribute("user", user);

		m.addAttribute("classEdit", true);
		return "profile";
	}
	


}
