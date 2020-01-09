package com.bookstore.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.bookstore.model.User;

@Repository
public interface UserRepository extends CrudRepository<User, Long> {
	
	User findByUsername(String username);
	
//	@Query("SELECT CASE WHEN COUNT(u) > 0 THEN true ELSE false END FROM User u "+"WHERE u.username = :username")
//	boolean existsbyUsername(@Param("username") String username);
//	
//	@Query("SELECT CASE WHEN COUNT(u) > 0 THEN true ELSE false END FROM User u "+"WHERE u.email = :email")
//	boolean existsbyEmail(@Param("email") String email);
//	
	User findByEmail(String email);
	
	
	
	

}
