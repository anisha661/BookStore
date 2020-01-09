package com.bookstore.controller;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.websocket.server.PathParam;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.bookstore.model.Book;
import com.bookstore.service.BookService;

@Controller
public class AdminController {
	
	@Autowired
	private BookService bookService;
	
	
	
	@RequestMapping("/adminportal")
	private String adminPortal() {
		return "admin/dashboard";
	}
	
	
	@RequestMapping(value="/addbook" ,method = RequestMethod.GET)
	private String addBook(Model m) {
		m.addAttribute("book", new Book());
		return "admin/addBook";
		
	}
	
	@RequestMapping(value="/addbook" ,method = RequestMethod.POST)
	public String addBookPost(@ModelAttribute("book") Book book,
			HttpServletRequest request) {
		
		bookService.save(book);
		
		MultipartFile bookImage=book.getBookImage();
		
		try {
			byte[] bytes= bookImage.getBytes();
			String name=book.getId() + ".png";
			BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(new File("src/main/resources/static/images/book/"+name)));
			stream.write(bytes);
			stream.close();
		} catch ( Exception e) {
		e.printStackTrace();
		}
	
	return "redirect:/booklist";
	
	
}
	
	@RequestMapping(value="/updatebook" ,method=RequestMethod.GET)
	public String updateBook(@RequestParam("id") long id ,Model m) {
		Book book =bookService.findById(id);
		m.addAttribute("book", book);
		return "admin/updatebook";
	}
	
	@RequestMapping(value="/updatebook" ,method=RequestMethod.POST)
	public String updateBookPost(@ModelAttribute("book") Book book ,HttpServletRequest request) {
		bookService.save(book);
		
		MultipartFile bookImage=book.getBookImage();
		
		try {
			byte[] bytes= bookImage.getBytes();
			String name=book.getId() + ".png";
			
			Files.delete(Paths.get("src/main/resources/static/images/book/"+name));
			BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(new File("src/main/resources/static/images/book/"+name)));
			stream.write(bytes);
			stream.close();
		} catch ( Exception e) {
		e.printStackTrace();
		}
		
		return "redirect:/bookinfo?id="+book.getId();
		
	}
	
	
	@RequestMapping("/booklist")
	public String bookList(Model m ) {
		
		List<Book> booklist = bookService.findAll();
		m.addAttribute("booklist", booklist);
		return "admin/booklist";
		
	}
	
	@RequestMapping("/bookinfo")
	public String getBookInfo(@PathParam("id")Long id,Model m) {
		Book book =bookService.findById(id);
		m.addAttribute("book", book);
		return "admin/bookinfo";
	}
	

}
