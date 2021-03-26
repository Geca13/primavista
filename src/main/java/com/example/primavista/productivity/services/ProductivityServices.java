package com.example.primavista.productivity.services;

import java.time.LocalDate;
import java.time.Month;
import java.time.Year;
import java.util.ArrayList;
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
import com.example.primavista.production.repository.LotRepository;
import com.example.primavista.production.repository.ProductOperationsRepository;
import com.example.primavista.production.repository.ProductRepository;
import com.example.primavista.productivity.entity.Employee;
import com.example.primavista.productivity.entity.Productivity;
import com.example.primavista.productivity.entity.Salary;
import com.example.primavista.productivity.repository.EmployeeRepository;
import com.example.primavista.productivity.repository.ProductivityRepository;
import com.example.primavista.productivity.repository.SalaryRepository;



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
	
	@Autowired
	SalaryRepository salaryRepository;
	
	@Autowired
	LotRepository lotRepository;
	
     public Page<Employee> findPagina(Integer pageNumber, Integer pageSize, String sortField, String sortDirection,String search) {
		
		Sort sort = sortDirection.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortField).ascending() :
			Sort.by(sortField).descending();
		Pageable pageable = PageRequest.of(pageNumber - 1, pageSize, sort);
		if(search != null) {
		    return	employeeRepository.findBySearch(search, pageable);
		}
		return employeeRepository.findAll(pageable);
	}
     
     public List<Productivity> findProductivity(Integer id, String date){
    	 
    	 LocalDate lastDate = productivityRepository.findAllByEmployeeIdOrderByProductivityDateDesc(id).get(0).getProductivityDate();
    	 
    return productivityRepository.findAllByEmployeeIdAndProductivityDate(id, lastDate);
    		 }
     
     
     public Productivity saveNewProductivity(Integer id,Integer oid,Productivity productivity) {
    	 Double sum = 0.00;
    	 Product product = productRepository.findById(id).get();
    	 ProductOpers operation = poRepository.findById(oid).get();
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
    	 productivityRepository.save(newProductivity);
    	 
    	 Month month = productivity.getProductivityDate().getMonth();
    	 Integer year = productivity.getProductivityDate().getYear();
    	 if(salaryRepository.existsByEmployeeAndMonthAndYear(newProductivity.getEmployee(), month,year)) {
    		 Salary salary = salaryRepository.findByEmployeeAndMonthAndYear(newProductivity.getEmployee(), month,year);
    		 Double thisMonthSalary = salary.getSalary()+ sum;
    		 salary.setSalary(thisMonthSalary);
    		 salary.getProductivities().add(newProductivity);
    		 salaryRepository.save(salary);
    		  return productivityRepository.save(newProductivity);
         }
    	 List<Productivity> newProductivityList = new ArrayList<>();
    	 newProductivityList.add(newProductivity);
    	 Salary salary = new Salary();
    	 salary.setEmployee(newProductivity.getEmployee());
    	 salary.setMonth(month);
    	 salary.setYear(year);
    	 salary.setProductivities(newProductivityList);
    	 salary.setSalary(sum);
    	 salaryRepository.save(salary);
    return productivityRepository.save(newProductivity);
     }
     
     
     public List<Productivity> productivityByEmployee(Integer id, LocalDate date){
    	 
    	 List<Productivity> list = productivityRepository.findAllByEmployeeIdAndProductivityDate(id, date);
    	 
		return list;
    	 
     }
     
     public void removeLotFromProductivity(Integer id,Integer lotId) {
    	 
    	 Productivity productivity = productivityRepository.findById(id).get();
    	 Lot lot = lotRepository.findById(lotId).get();
    	 productivity.getLots().remove(lot);
    	 productivity.setProductivitySum(productivity.getProductivitySum()-(productivity.getOperation().getOperationValue()*lot.getQty()));
    	 productivityRepository.save(productivity);
    	 Salary salary = salaryRepository.findByMonth(productivity.getProductivityDate().getMonth());
    	 salary.setSalary(salary.getSalary()-(productivity.getOperation().getOperationValue()*lot.getQty()));
    	 salaryRepository.save(salary);
		 if(productivity.getLots().isEmpty()) {
			productivityRepository.delete(productivity);
		 }
		 
	}
     
     public void deleteProductivity(Integer id) {
    	 
    	 Productivity productivity = productivityRepository.findById(id).get();
    	 Salary salary = salaryRepository.findByMonth(productivity.getProductivityDate().getMonth());
    	 salary.getProductivities().remove(productivity);
    	 salary.setSalary(salary.getSalary()-productivity.getProductivitySum());
    	 salaryRepository.save(salary);
         productivity.setLots(null);
         productivity.setOperation(null);
         productivityRepository.delete(productivity);
    	 
    	 
     }
   

}
