package com.example.primavista.productivity.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.example.primavista.productivity.entity.Employee;
import com.example.primavista.productivity.repository.EmployeeRepository;



@Service
public class ProductivityServices {
	
	@Autowired
	EmployeeRepository employeeRepository;
	
     public Page<Employee> findPagina(Integer pageNumber, Integer pageSize, String sortField, String sortDirection,String search) {
		
		Sort sort = sortDirection.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortField).ascending() :
			Sort.by(sortField).descending();
		Pageable pageable = PageRequest.of(pageNumber - 1, pageSize, sort);
		if(search != null) {
		    return	employeeRepository.findBySearch(search, pageable);
		}
		return employeeRepository.findAll(pageable);
	}

}
