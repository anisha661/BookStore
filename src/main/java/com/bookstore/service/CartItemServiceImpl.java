package com.bookstore.service;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bookstore.model.Book;
import com.bookstore.model.BookToCartItem;
import com.bookstore.model.CartItem;
import com.bookstore.model.ShoppingCart;
import com.bookstore.model.User;
import com.bookstore.repository.BookToCartItemRepository;
import com.bookstore.repository.CartItemRepository;

@Service
public class CartItemServiceImpl implements CartItemService {

	@Autowired
	private CartItemRepository cartRepository;
	
	@Autowired
	private BookToCartItemRepository bookToCartItemRepository;
	
	@Override
	public List<CartItem> findByShoppingCart(ShoppingCart shoppingCart) {
		return cartRepository.findByShoppingCart(shoppingCart);
	}

	@Override
	public CartItem updateCartItem(CartItem cartItem) {
        BigDecimal bigDecimal = new BigDecimal(cartItem.getBook().getOurPrice()).multiply(new BigDecimal(cartItem.getQuantity()));	
        
       
	cartItem.setSubTotal(bigDecimal.setScale(2,BigDecimal.ROUND_HALF_UP));
        cartRepository.save(cartItem);
        
        return cartItem;
        
	}

	@Override
	public CartItem addBookToCart(Book book, User user, int quantity) {
		List<CartItem> cartItemList = findByShoppingCart(user.getShoppingCart());
		
		for (CartItem cartItem : cartItemList) {
			
			if(book.getId()==cartItem.getBook().getId()) {
				cartItem.setQuantity(cartItem.getQuantity()+quantity);
				cartItem.setSubTotal(new BigDecimal(book.getOurPrice()).multiply(new BigDecimal(quantity)));
				cartRepository.save(cartItem);
				return cartItem;
			}
			
		}
		
		CartItem cartItem = new CartItem();
		cartItem.setShoppingCart(user.getShoppingCart());
		cartItem.setBook(book);

		cartItem.setQuantity(quantity);
		cartItem.setSubTotal(new BigDecimal(book.getOurPrice()).multiply(new BigDecimal(quantity)));
		
		
		cartItem= cartRepository.save(cartItem);
		
		BookToCartItem bookToCartItem= new BookToCartItem();
		bookToCartItem.setBook(book);
		bookToCartItem.setCartItem(cartItem);
		bookToCartItemRepository.save(bookToCartItem);
		
		return cartItem;
	}

	@Override
	public CartItem findById(long cartItemId) {
		return cartRepository.findById(cartItemId).get();
	}

	@Override
	public void removecartItem(CartItem cartItem) {
      bookToCartItemRepository.deleteByCartItem(cartItem);
      cartRepository.delete(cartItem);
	}

	@Override
	public CartItem save(CartItem cartItem) {

		 return cartRepository.save(cartItem);
		
	}

}
