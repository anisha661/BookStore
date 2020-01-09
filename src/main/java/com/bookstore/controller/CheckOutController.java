package com.bookstore.controller;

import java.security.Principal;
import java.time.LocalDate;
import java.util.List;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.bookstore.model.BillingAddress;
import com.bookstore.model.CartItem;
import com.bookstore.model.Order;
import com.bookstore.model.Payment;
import com.bookstore.model.ShippingAddress;
import com.bookstore.model.ShoppingCart;
import com.bookstore.model.User;
import com.bookstore.model.UserBilling;
import com.bookstore.model.UserPayment;
import com.bookstore.model.UserShipping;
import com.bookstore.service.BillingAddressService;
import com.bookstore.service.CartItemService;
import com.bookstore.service.OrderService;
import com.bookstore.service.PaymentService;
import com.bookstore.service.ShippingAddressService;
import com.bookstore.service.ShoppingCartService;
import com.bookstore.service.UserPaymentService;
import com.bookstore.service.UserService;
import com.bookstore.service.UserShippingService;
import com.bookstore.utility.MailConstructor;


@Controller
public class CheckOutController {
	
	
	
	private ShippingAddress shippingAddress= new ShippingAddress();
	private BillingAddress billingAddress= new BillingAddress();
	private Payment payment = new Payment();
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private CartItemService cartItemService;
	
	@Autowired
	private ShippingAddressService shippingAddressService;
	
	@Autowired
	private PaymentService paymentService;
	
	@Autowired
	private BillingAddressService billingAddressService;
	
	@Autowired
	private UserShippingService userShippingService;
	
	@Autowired
	private UserPaymentService userPaymentService;
	
	@Autowired
	private JavaMailSender mailSender;
	
	@Autowired
	private ShoppingCartService shoppingCartService;
	
	@Autowired
	private OrderService orderService;
	
	@Autowired
	private MailConstructor mailConstructor;
	
	@RequestMapping("/checkout")
	public String checkout(
			@RequestParam("id") Long cartId,
			@RequestParam(value="missingRequiredField" ,required =false )boolean missingRequiredField,
			Model m ,Principal p) {
		
		User user =userService.findbyUsername(p.getName());
		
		if(cartId != user.getShoppingCart().getId()) {
			return "badRequestPage";
		}
		
		List<CartItem> cartItemList = cartItemService.findByShoppingCart(user.getShoppingCart());
		
		if(cartItemList.size()==0) {
			m.addAttribute("emptyCart", true);
			return "forward:/cart";
		}
		
		for (CartItem cartItem : cartItemList) {
			if(cartItem.getBook().getInStock()<cartItem.getQuantity()) {
				m.addAttribute("notEnoughStock", true);
				return "forward:/cart";
			}
		}
		
		List<UserShipping> userShippingList = user.getUserShippingList();
		List<UserPayment> userPaymentList= user.getUserPaymentList();
		
		m.addAttribute("userShippingList", userShippingList);
		m.addAttribute("userPaymentList", userPaymentList);
		
		if(userPaymentList.size()==0) {
			m.addAttribute("emptyPaymentList", true);
		}else {
			m.addAttribute("emptyPaymentList", false);
		}
		
		
		if(userShippingList.size()==0) {
			m.addAttribute("emptyShippingList", true);
		}else {
			m.addAttribute("emptyShippingList", false);
		}
		
		
		ShoppingCart shoppingCart =user.getShoppingCart();
		for (UserShipping userShipping : userShippingList) {
			if(userShipping.isShippingDefault()) {
				shippingAddressService.setByUserShipping(userShipping,shippingAddress);
			}
			
		}
		
		
		for (UserPayment userPayment : userPaymentList) {
			if(userPayment.isDefaultPayment()) {
				paymentService.setByUserPayment(userPayment,payment);
				billingAddressService.setByUserBilling(userPayment.getUserBilling(),billingAddress);
			}
			
		}
		
		m.addAttribute("shippingAddress", shippingAddress);
		m.addAttribute("billingAddress", billingAddress);
		m.addAttribute("payment", payment);
		m.addAttribute("cartItemList", cartItemList);
		m.addAttribute("shoppingCart", shoppingCart);
		
		m.addAttribute("classShipping", true);
		if(missingRequiredField) {
			m.addAttribute("missingRequiredField", true);
		}
		return "checkout";
	}
	
	@RequestMapping(value = "/checkout" ,method=RequestMethod.POST)
	public String checkoutPost(@ModelAttribute("shippingAddress") ShippingAddress shippingAddress,
			@ModelAttribute("billingAddress") BillingAddress billingAddress,
			@ModelAttribute("payment") Payment payment ,
			@ModelAttribute("billingSameAsShipping") String billingSameAsShipping,
			@ModelAttribute("shippingMethod") String shippingMethod,
			Principal p ,Model m) {
		
		ShoppingCart shoppingCart =userService.findbyUsername(p.getName()).getShoppingCart();
		List<CartItem> cartItemList = cartItemService.findByShoppingCart(shoppingCart);
		m.addAttribute("cartItemList", cartItemList);
		
		if(billingSameAsShipping.equals("true")){
			billingAddress.setBillingAddressName(shippingAddress.getShippingAddressName());
			billingAddress.setBillingAddressStreet(shippingAddress.getShippingAddressStreet());
			billingAddress.setBillingAddressCity(shippingAddress.getShippingAddressCity());
			billingAddress.setBillingAddressCountry(shippingAddress.getShippingAddressCountry());
			
		}
		
		if(shippingAddress.getShippingAddressStreet().isEmpty() || shippingAddress.getShippingAddressName().isEmpty() ||shippingAddress.getShippingAddressCity().isEmpty()
				|| shippingAddress.getShippingAddressCountry().isEmpty() || payment.getCardNumber().isEmpty() ||
				billingAddress.getBillingAddressCity().isEmpty() ||billingAddress.getBillingAddressCountry().isEmpty() ||
				billingAddress.getBillingAddressName().isEmpty() ||billingAddress.getBillingAddressStreet().isEmpty()) {
			
			return "redirect:/checkout?id="+shoppingCart.getId()+"&missingRequiredField=true";
		}
		
		User user = userService.findbyUsername(p.getName());
		
		Order order = orderService.createOrder(shoppingCart,shippingAddress,billingAddress,payment ,shippingMethod,user);
		
		mailSender.send(mailConstructor.constructOrderConfirmationEmail(user,order,Locale.ENGLISH));
		
		shoppingCartService.clearShoppingCart(shoppingCart);
		
		LocalDate today= LocalDate.now();
		LocalDate estimatedDelivery;
		
		if(shippingMethod.equals("groundShipping")) {
			estimatedDelivery = today.plusDays(4);
		}else {
			estimatedDelivery=today.plusDays(2);
		}
		
		m.addAttribute("estimatedDeliveryDate", estimatedDelivery);
		
		
		return "orderPage";
	}
	
	
	
	
	
	
	
	
	
	
	
	@RequestMapping("/setShippingAddress")
	public String setShippingAddress(@RequestParam("userShippingId") Long userShippingId,Principal p ,Model m ) {
		
		User user = userService.findbyUsername(p.getName());
		UserShipping userShipping= userShippingService.findById(userShippingId);
		
		if(userShipping.getUser().getId() != user.getId()) {
			return "badRequestPage";
		}else {
			shippingAddressService.setByUserShipping(userShipping, shippingAddress);
			
			List<CartItem> cartItemList = cartItemService.findByShoppingCart(user.getShoppingCart());			
			m.addAttribute("shippingAddress", shippingAddress);
			m.addAttribute("billingAddress", billingAddress);
			m.addAttribute("payment", payment);
			m.addAttribute("cartItemList", cartItemList);
			m.addAttribute("shoppingCart", user.getShoppingCart());
			
			

			List<UserShipping> userShippingList = user.getUserShippingList();
			List<UserPayment> userPaymentList= user.getUserPaymentList();
			
			m.addAttribute("userShippingList", userShippingList);
			m.addAttribute("userPaymentList", userPaymentList);
			
			
			m.addAttribute("shippingAddress", shippingAddress);
			m.addAttribute("classShipping", true);
			
			if(userPaymentList.size()==0) {
				m.addAttribute("emptyPaymentList", true);
			}else {
				m.addAttribute("emptyPaymentList", false);
			}
			
		    m.addAttribute("emptyShippingList", false);
			
			
			return "checkout";
		}
		
	}
	
	
	@RequestMapping("/setPaymentMethod")
	public String setPaymentMethod(@RequestParam("userPaymentId") Long userPaymentId,Principal p ,Model m ) {
		
		User user = userService.findbyUsername(p.getName());
		UserPayment userPayment= userPaymentService.findById(userPaymentId);
		
		UserBilling userBilling = userPayment.getUserBilling();
		
		if(userPayment.getUser().getId() != user.getId()) {
			return "badRequestPage";
		}else {
			paymentService.setByUserPayment(userPayment, payment);
			
			List<CartItem> cartItemList = cartItemService.findByShoppingCart(user.getShoppingCart());
			billingAddressService.setByUserBilling(userBilling, billingAddress);
			
			m.addAttribute("shippingAddress", shippingAddress);
			m.addAttribute("billingAddress", billingAddress);
			m.addAttribute("payment", payment);
			m.addAttribute("cartItemList", cartItemList);
			m.addAttribute("shoppingCart", user.getShoppingCart());
			
			

			List<UserShipping> userShippingList = user.getUserShippingList();
			List<UserPayment> userPaymentList= user.getUserPaymentList();
			
			m.addAttribute("userShippingList", userShippingList);
			m.addAttribute("userPaymentList", userPaymentList);
			
			
			m.addAttribute("shippingAddress", shippingAddress);
			m.addAttribute("classPayment", true);
			
			
		    m.addAttribute("emptyPaymentList", false);
			if(userShippingList.size()==0) {
				m.addAttribute("emptyShippingList", true);
			}else {
				m.addAttribute("emptyShippingList", false);
			}
			
			return "checkout";
		}
		
		
		
	}
	
	

	
	
	
}
