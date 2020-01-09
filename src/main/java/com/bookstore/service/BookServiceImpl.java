package com.bookstore.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bookstore.model.Book;
import com.bookstore.repository.BookRepository;

@Service
public class BookServiceImpl implements BookService {

	@Autowired
	private BookRepository bookRepository;
	
	@Override
	public Book save(Book book) {
	
		return bookRepository.save(book);
	}

	@Override
	public List<Book> findAll() {
	
		return (List<Book>) bookRepository.findAll();
	}

	@Override
	public Book findById(long id) {
		
		return bookRepository.findById(id).get();
	}

}
