package com.bookstore.service;

import java.util.List;

import com.bookstore.model.Book;

public interface BookService {
	
	Book save(Book book);
	
	List<Book> findAll();

	Book findById(long id);

}
