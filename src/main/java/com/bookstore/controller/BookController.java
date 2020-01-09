package com.bookstore.controller;

import java.security.Principal;
import java.util.Arrays;
import java.util.List;

import javax.websocket.server.PathParam;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.bookstore.model.Book;
import com.bookstore.model.User;
import com.bookstore.service.BookService;
import com.bookstore.service.UserService;

@Controller
public class BookController {
	
	@Autowired
	private BookService bookService;
	
	@Autowired
	private UserService userService;
	
	
	@RequestMapping(value="/bookshelf" ,method = RequestMethod.GET)
	public String bookshelf(Model m) {
		
		List<Book> books=bookService.findAll();
		m.addAttribute("listOfBook", books);
		return "bookshelf";
	}
	
	@RequestMapping("/bookdetail")
	public String bookDetail(@PathParam("id") long id,Model m ,Principal p) {
		if(p !=null) {
			String username=p.getName();
			User user= userService.findbyUsername(username);
			m.addAttribute("user", user);
		}
		
		Book book = bookService.findById(id);
		m.addAttribute("book", book);
		List<Integer> quantitylist =Arrays.asList(1,2,3,4,5,6,7,8,9,10);
		m.addAttribute("quantitylist" ,quantitylist);
		m.addAttribute("quantity", 1);
		return "bookdetail";
	}

}
