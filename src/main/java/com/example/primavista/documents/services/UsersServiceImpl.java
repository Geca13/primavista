package com.example.primavista.documents.services;




import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.primavista.documents.entity.RoleName;
import com.example.primavista.documents.entity.User;
import com.example.primavista.documents.repository.RoleRepository;
import com.example.primavista.documents.repository.UserRepository;

import javax.transaction.Transactional;


@Service
@Transactional
public class UsersServiceImpl implements UsersService {
	
	@Autowired
	private BCryptPasswordEncoder passwordEncoder;
	
	@Autowired
	RoleRepository roleRepository;
	
	
	@Autowired
	private UserRepository userRepository;

	
	
	



	@Override
	public User save(User userDto) {
		
		User user = new User();
		user.setEmail(userDto.getEmail());
		user.setPassword(passwordEncoder.encode(userDto.getPassword()));
		user.setFirstName(userDto.getFirstName());
        user.setLastName(userDto.getLastName());
        user.setRole(RoleName.ROLE_USER);
		
		return userRepository.save(user);
	}
	
	



	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		
		User user = userRepository.findByEmail(username);
		
		if(user == null) {
			throw new UsernameNotFoundException("You are not signUped with that email");
		}
		
		return new UsersDetails(user);
	}
	
	



	@Override
	public Page<User> findPagina(Integer pageNumber, Integer pageSize, String sortField, String sortDirection) {
		
		Sort sort = sortDirection.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortField).ascending() :
			Sort.by(sortField).descending();
		Pageable pageable = PageRequest.of(pageNumber - 1, pageSize, sort);
		
		return userRepository.findAll(pageable);
	}
	
	

}
