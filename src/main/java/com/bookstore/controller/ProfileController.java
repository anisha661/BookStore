package com.bookstore.controller;

import java.security.Principal;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
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

import com.bookstore.model.User;
import com.bookstore.model.UserBilling;
import com.bookstore.model.UserPayment;
import com.bookstore.model.UserShipping;
import com.bookstore.service.UserDetailsServiceImpl;
import com.bookstore.service.UserPaymentService;
import com.bookstore.service.UserService;
import com.bookstore.service.UserServiceImpl;
import com.bookstore.service.UserShippingService;

@Controller
public class ProfileController {
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private UserPaymentService paymentService;
	
	@Autowired
	private UserShippingService shippingService;
	
	@Autowired
	private BCryptPasswordEncoder encoder;
	
	@Autowired
	private UserDetailsServiceImpl service;
	

	@RequestMapping("/profile")
	public String profile(Model m ,Principal p ) {
		
		User user = userService.findbyUsername(p.getName());
		m.addAttribute("user", user);
		m.addAttribute("userPaymentList", user.getUserPaymentList());
		m.addAttribute("userShippingList", user.getUserShippingList());
//		m.addAttribute("orderList",user.geto );
		
		UserShipping shipping =new UserShipping();
		m.addAttribute("userShipping", shipping);
		m.addAttribute("listOfCreditCard", true);
		m.addAttribute("listOfShoppingAddress", true);
		m.addAttribute("classEdit", true);
		return "profile" ;
	}
	
	
	@RequestMapping("/listOfCreditCard")
	public String listOfCreditCards(Model m ,Principal p,HttpServletRequest request) {
		User user = userService.findbyUsername(p.getName());
		m.addAttribute("user", user);
		
		m.addAttribute("userPaymentList", user.getUserPaymentList());
		m.addAttribute("userShippingList", user.getUserShippingList());
//		m.addAttribute("orderList",user.geto );
		
		m.addAttribute("listOfCreditCard", true);
		m.addAttribute("classBilling", true);
		m.addAttribute("listOfShippingAddress", true);
		
		return "profile";
		
	}
	
	@RequestMapping(value="/updateUserInfo" ,method=RequestMethod.POST)
	public String updateUserInfo(@ModelAttribute("user") User user ,@ModelAttribute("address") String address,
	@ModelAttribute("phoneno") String phoneno ,Model m) throws Exception{
		
		User currentUser = userService.findById(user.getId());
		
		if(currentUser == null) {
			throw new Exception("User not found");
		}
		
		if(userService.findByEmail(user.getEmail())!=null) {
			if(userService.findByEmail(user.getEmail()).getId() != currentUser.getId()) {
				m.addAttribute("emailExists", true);
				return "profile";
			}
		}
		
		if(userService.findbyUsername(user.getUsername())!=null) {
			if(userService.findbyUsername(user.getUsername()).getId() != currentUser.getId()) {
				m.addAttribute("usernameExists", true);
				return "profile";
			}
		}
		
		currentUser.setName(user.getName());
		currentUser.setEmail(user.getEmail());
		currentUser.setUsername(user.getUsername());
		currentUser.setPassword(user.getPassword());
		currentUser.setAddress(address);
		currentUser.setPhoneno(phoneno);
		currentUser.setEnabled(true);
		
		userService.saveUser(currentUser);
		
		m.addAttribute("updateSuccess", true);
		m.addAttribute("user", currentUser);
		m.addAttribute("classEdit", true);
		
		UserDetails userDetails = service.loadUserByUsername(currentUser.getUsername());

		Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, userDetails.getPassword(),
				userDetails.getAuthorities());
		
		SecurityContextHolder.getContext().setAuthentication(authentication);
		
		return "profile";
	}
	
	
	
	
	
	@RequestMapping("/addNewCreditCard")
	public String addCreditCard(Model m ,Principal p) {
		
		User user= userService.findbyUsername(p.getName());
		m.addAttribute("user", user);
		
		m.addAttribute("addNewCreditCard", true);
		m.addAttribute("classBilling", true);
		m.addAttribute("listOfShippingAddress", true);
		
		m.addAttribute("userBilling", new UserBilling());
		m.addAttribute("userPayment", new UserPayment());
		
		
		m.addAttribute("userPaymentList", user.getUserPaymentList());
		m.addAttribute("userShippingList", user.getUserShippingList());
		
		return "profile";
		
	}
	
	
	

	@RequestMapping(value="/addNewCreditCard" ,method = RequestMethod.POST)
	public String addCreditCard(@ModelAttribute("userPayment") UserPayment userPayment,
			@ModelAttribute("userBilling") UserBilling userBilling,Principal p ,Model m) {
		User user = userService.findbyUsername(p.getName());
		userService.updateUserBilling(userBilling,userPayment,user);
		m.addAttribute("user", user);
		
		m.addAttribute("userPaymentList", user.getUserPaymentList());
		m.addAttribute("userShippingList", user.getUserShippingList());
		
		m.addAttribute("listOfCreditCard", true);
		m.addAttribute("classBilling", true);
		m.addAttribute("listOfShippingAddress", true);
		
		return "profile";
		
	}
	
	
	@RequestMapping("/updateCreditCard")
	public String updateCreditCard(@ModelAttribute("id")Long cardId, Principal p ,Model m ) {
		
		User user= userService.findbyUsername(p.getName());
		UserPayment userPayment = paymentService.findById(cardId);
		
		if(user.getId()!=userPayment.getUser().getId()) {
			return "badRequestPage";
		}else {
			m.addAttribute("user", user);
			UserBilling userBilling= userPayment.getUserBilling();
			m.addAttribute("userBilling", userBilling);
			m.addAttribute("userPayment", userPayment);
			
			m.addAttribute("addNewCreditCard", true);
			m.addAttribute("classBilling", true);
			m.addAttribute("listOfShippingAddress", true);
			
			m.addAttribute("userPaymentList", user.getUserPaymentList());
			m.addAttribute("userShippingList", user.getUserShippingList());
			
			return "profile";
			
			
		}
		
	}
	
	
	@RequestMapping(value="/setDefaultPayment" ,method = RequestMethod.POST)
	public String setDefaultPayment(@ModelAttribute("defaultPaymentId")Long defaultPaymentId, Principal p ,Model m) {
		
		User user= userService.findbyUsername(p.getName());
		paymentService.setDefaultPayment(defaultPaymentId,user);
			m.addAttribute("user", user);
			
			
			m.addAttribute("listOfCreditCard", true);
			m.addAttribute("classBilling", true);
			m.addAttribute("listOfShippingAddress", true);
			
			m.addAttribute("userPaymentList", user.getUserPaymentList());
			m.addAttribute("userShippingList", user.getUserShippingList());
			
			return "profile";
		
		
	}
	
	
	
	
	@RequestMapping("/removeCreditCard")
	public String removeCreditCard(@ModelAttribute("id")Long cardId, Principal p ,Model m) {
		
		User user= userService.findbyUsername(p.getName());
		UserPayment userPayment = paymentService.findById(cardId);
		
		if(user.getId()!=userPayment.getUser().getId()) {
			return "badRequestPage";
		}else {
			m.addAttribute("user", user);
			paymentService.removeCreditCard(cardId);
			
			m.addAttribute("listOfCreditCard", true);
			m.addAttribute("classBilling", true);
			m.addAttribute("listOfShippingAddress", true);
			
			m.addAttribute("userPaymentList", user.getUserPaymentList());
			m.addAttribute("userShippingList", user.getUserShippingList());
			
			return "profile";
			
			
		}
		
	}
	
	@RequestMapping("/listOfShippingAddress")
	public String listOfShippingAddress(Model m ,Principal p,HttpServletRequest request) {
		User user = userService.findbyUsername(p.getName());
		m.addAttribute("user", user);
		m.addAttribute("userPaymentList", user.getUserPaymentList());
		m.addAttribute("userShippingList", user.getUserShippingList());
//		m.addAttribute("orderList",user.geto );
		
		m.addAttribute("listOfCreditCard", true);
		m.addAttribute("classShipping", true);
		m.addAttribute("listOfShippingAddress", true);
		return "profile";
		
	}
	
	
	
	
	@RequestMapping("/addNewShippingAddress")
	public String addShippingAddress(Model m ,Principal p) {
		
		User user= userService.findbyUsername(p.getName());
		m.addAttribute("user", user);
		
		m.addAttribute("addNewShippingAddress", true);
		m.addAttribute("classShipping", true);
		
		
		m.addAttribute("userShipping", new UserShipping());
	
		
		
		m.addAttribute("userPaymentList", user.getUserPaymentList());
		m.addAttribute("userShippingList", user.getUserShippingList());
		
		return "profile";
		
	}
	
	@RequestMapping(value="/addNewShippingAddress" ,method = RequestMethod.POST)
	public String addNewShippingAddress(@ModelAttribute("userShipping") UserShipping userShipping,Model m ,Principal p) {
		
		User user= userService.findbyUsername(p.getName());
		
		userService.updateUserShipping(userShipping,user);
		m.addAttribute("user", user);
		
		
		m.addAttribute("classShipping", true);
		m.addAttribute("listOfCreditCard", true);
		m.addAttribute("listOfShippingAddress", true);
		m.addAttribute("userPaymentList", user.getUserPaymentList());
		m.addAttribute("userShippingList", user.getUserShippingList());
		
		return "profile";
		
	}
	
	@RequestMapping("/updateShipping")
	public String updateShipping(@ModelAttribute("id")Long shippingId, Principal p ,Model m ) {
		
		User user= userService.findbyUsername(p.getName());
		UserShipping userShipping = shippingService.findById(shippingId);
		
		if(user.getId()!=userShipping.getUser().getId()) {
			return "badRequestPage";
		}else {
			m.addAttribute("user", user);
			
			m.addAttribute("userShipping", userShipping);
			
			m.addAttribute("addNewShippingAddress", true);
			m.addAttribute("classShipping", true);
			m.addAttribute("listOfShippingAddress", true);
			
			m.addAttribute("userPaymentList", user.getUserPaymentList());
			m.addAttribute("userShippingList", user.getUserShippingList());
			
			return "profile";
			
			
		}
		
	}
	
	
	@RequestMapping(value="/setDefaultShippingAddress" ,method = RequestMethod.POST)
	public String setDefaultShippingAddress(@ModelAttribute("defaultShippingAddressId")Long defaultShippingAddressId, Principal p ,Model m) {
		
		User user= userService.findbyUsername(p.getName());
		shippingService.setDefaultShippingAddress(defaultShippingAddressId,user);
			m.addAttribute("user", user);
			
			
			m.addAttribute("listOfCreditCard", true);
			m.addAttribute("classShipping", true);
			m.addAttribute("listOfShippingAddress", true);
			
			m.addAttribute("userPaymentList", user.getUserPaymentList());
			m.addAttribute("userShippingList", user.getUserShippingList());
			
			return "profile";
		
		
	}
	
	
	
	
	@RequestMapping("/removeShipping")
	public String removeShipping(@ModelAttribute("id")Long shippingId, Principal p ,Model m) {
		
		User user= userService.findbyUsername(p.getName());
		UserShipping userShipping = shippingService.findById(shippingId);
		
		if(user.getId()!=userShipping.getUser().getId()) {
			return "badRequestPage";
		}else {
			m.addAttribute("user", user);
			shippingService.removeShippingAddress(shippingId);
			
			m.addAttribute("listOfCreditCard", true);
			m.addAttribute("classShipping", true);
			m.addAttribute("listOfShippingAddress", true);
			
			m.addAttribute("userPaymentList", user.getUserPaymentList());
			m.addAttribute("userShippingList", user.getUserShippingList());
			
			return "profile";
			
			
		}
		
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	

}
