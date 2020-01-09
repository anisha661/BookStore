package com.bookstore.repository;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.repository.CrudRepository;

import com.bookstore.model.CartItem;
import com.bookstore.model.ShoppingCart;

@Transactional
public interface CartItemRepository extends CrudRepository<CartItem,Long> {

	
	List<CartItem> findByShoppingCart(ShoppingCart shoppingCart);
	
}
