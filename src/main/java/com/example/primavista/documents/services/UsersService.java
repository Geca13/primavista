package com.example.primavista.documents.services;

import org.springframework.data.domain.Page;
import org.springframework.security.core.userdetails.UserDetailsService;

import com.example.primavista.documents.entity.User;


public interface UsersService extends UserDetailsService {
	
	User save(User userDto);
	
	public Page<User> findPagina(Integer pageNumber, Integer pageSize,String sortField, String sortDirection);

}
