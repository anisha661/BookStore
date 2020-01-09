package com.bookstore.controller;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.bookstore.model.Book;
import com.bookstore.model.CartItem;
import com.bookstore.model.ShoppingCart;
import com.bookstore.model.User;
import com.bookstore.service.BookService;
import com.bookstore.service.CartItemService;
import com.bookstore.service.ShoppingCartService;
import com.bookstore.service.UserService;

@Controller

public class ShoppingCartController {
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private CartItemService cartItemService;
	
	@Autowired
	private ShoppingCartService shoppingCartService;
	
	@Autowired
	private BookService bookService;
	
	@RequestMapping("/cart")
	public String shoppingCart(Model m ,Principal p) {
		User user = userService.findbyUsername(p.getName());
		ShoppingCart shoppingCart = user.getShoppingCart();
		List<CartItem> cartItemList= cartItemService.findByShoppingCart(shoppingCart);
		
		shoppingCartService.updateShoppingCart(shoppingCart);
		
		m.addAttribute("cartItemList", cartItemList);
		m.addAttribute("shoppingCart", shoppingCart);
		
		return "shoppingCart";
	}

	@RequestMapping("/addItem")
	public String addItem(
			@ModelAttribute("book") Book book,
			@ModelAttribute("quantity") String quantity,
			Model m ,Principal p
			
			) {
		
		User user = userService.findbyUsername(p.getName());
		book=bookService.findById(book.getId());
		
		if(Integer.parseInt(quantity) > book.getInStock()) {
			m.addAttribute("notEnoughStock", true);
			return "forward:/bookdetail?id="+book.getId();
		}
		CartItem cartItem =cartItemService.addBookToCart(book,user,Integer.parseInt(quantity));
		m.addAttribute("addBookSuccess", true);
		
		return "forward:/bookdetail?id="+book.getId();
	}
	
	
	@RequestMapping("/updateItem")
	public String updateItem(
			@ModelAttribute("id")long cartItemId,
			@ModelAttribute("quantity") int quantity,
			Model m ,Principal p
			
			) {
		
		User user = userService.findbyUsername(p.getName());
		
		CartItem cartItem = cartItemService.findById(cartItemId);
		cartItem.setQuantity(quantity);
		cartItemService.updateCartItem(cartItem);
		
		return "forward:/cart";
	}
	
	
	@RequestMapping("removeItem")
	public String removeItem(@RequestParam("id") Long id) {
		
		cartItemService.removecartItem(cartItemService.findById(id));
		
		
		return "forward:/cart";
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
