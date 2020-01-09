package com.bookstore.service;

import java.util.List;

import com.bookstore.model.Book;
import com.bookstore.model.CartItem;
import com.bookstore.model.ShoppingCart;
import com.bookstore.model.User;

public interface CartItemService {

	List<CartItem> findByShoppingCart(ShoppingCart shoppingCart);

	CartItem updateCartItem(CartItem cartItem);

	CartItem addBookToCart(Book book, User user, int quantity);

	CartItem findById(long cartItemId);

	void removecartItem(CartItem cartItem);

	CartItem save(CartItem cartItem);

}
