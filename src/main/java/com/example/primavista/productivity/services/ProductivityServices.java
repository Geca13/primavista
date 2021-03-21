package com.example.primavista.productivity.services;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.example.primavista.production.entity.Lot;
import com.example.primavista.production.entity.Product;
import com.example.primavista.production.entity.ProductOpers;
import com.example.primavista.production.repository.ProductOperationsRepository;
import com.example.primavista.production.repository.ProductRepository;
import com.example.primavista.productivity.entity.Employee;
import com.example.primavista.productivity.entity.Productivity;
import com.example.primavista.productivity.repository.EmployeeRepository;
import com.example.primavista.productivity.repository.ProductivityRepository;



@Service
public class ProductivityServices {
	
	@Autowired
	EmployeeRepository employeeRepository;
	
	@Autowired
	ProductivityRepository productivityRepository;
	
	@Autowired
	ProductRepository productRepository;
	
	@Autowired
	ProductOperationsRepository poRepository;
	
     public Page<Employee> findPagina(Integer pageNumber, Integer pageSize, String sortField, String sortDirection,String search) {
		
		Sort sort = sortDirection.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortField).ascending() :
			Sort.by(sortField).descending();
		Pageable pageable = PageRequest.of(pageNumber - 1, pageSize, sort);
		if(search != null) {
		    return	employeeRepository.findBySearch(search, pageable);
		}
		return employeeRepository.findAll(pageable);
	}
     
     public Productivity saveNewProductivity(Integer id,Integer oid,Productivity productivity) {
    	 Double sum = 0.00;
    	 Product product = productRepository.findById(id).get();
    	 ProductOpers operation = poRepository.findById(oid).get();
    	 
    	 
    	 if(productivityRepository.existsByEmployee(productivity.getEmployee())) {
    		 
    	 }
    	 Productivity newProductivity = new Productivity();
    	 newProductivity.setProduct(product);
    	 newProductivity.setEmployee(productivity.getEmployee());
    	 newProductivity.setLots(productivity.getLots());
    	 newProductivity.setProductivityDate(productivity.getProductivityDate());
    	 newProductivity.setOperation(operation);
    	 for (Lot lot : productivity.getLots()) {
    		 sum = sum + (lot.getQty() * operation.getOperationValue());
    		 newProductivity.setProductivitySum(sum);
         }
    return productivityRepository.save(newProductivity);
    }
     
     public List<Productivity> productivityByEmployeeByPeriod(Integer id, LocalDate date){
    	 
    	 List<Productivity> list = productivityRepository.findAllByEmployeeIdAndProductivityDate(id, date);
    	 
		return list;
    	 
     }

}
