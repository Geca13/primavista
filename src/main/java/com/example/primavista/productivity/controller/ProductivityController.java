package com.example.primavista.productivity.controller;

import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import com.example.primavista.production.entity.Lot;
import com.example.primavista.production.entity.ProductOpers;
import com.example.primavista.production.repository.LotRepository;
import com.example.primavista.production.repository.ProductOperationsRepository;
import com.example.primavista.production.repository.ProductRepository;
import com.example.primavista.productivity.entity.Productivity;
import com.example.primavista.productivity.entity.Salary;
import com.example.primavista.productivity.repository.EmployeeRepository;
import com.example.primavista.productivity.repository.ProductivityRepository;
import com.example.primavista.productivity.repository.SalaryRepository;
import com.example.primavista.productivity.services.ProductivityServices;

@Controller
public class ProductivityController {

	@Autowired
	ProductivityServices prodServices;
	
	@Autowired
	EmployeeRepository employeeRepository;
	
	@Autowired
	ProductivityRepository productivityRepository;
	
	@Autowired
	ProductRepository productRepository;
	
	@Autowired
	ProductOperationsRepository poRepository;
	
	@Autowired
	LotRepository lotRepository;
	
	@Autowired
	SalaryRepository salaryRepository;
	
	@GetMapping("/addNewProductivity/{id}/{oid}")
	@Transactional
	public String getNewProductivityPage(Model model,@PathVariable("id") Integer id,@PathVariable("oid") Integer oid) {
		
		List<ProductOpers> operations = poRepository.findAllByProductId(id);
		List<Lot> lots = new ArrayList<>();// mora nova lista za da se sprechi concurrentModificationException koja se javuva koga se obiduvas da promenis lista
		List<Lot> oldlots = lotRepository.findAllByProductId(id);
		for (Lot lot : oldlots) {
			if(!productivityRepository.existsByOperationAndLots_Id(poRepository.findById(oid).get(), lot.getId())) {
				lots.add(lot);
			}
		}
		model.addAttribute("productivity", new Productivity());
		model.addAttribute("operations", operations);
		model.addAttribute("lots", lots);
		model.addAttribute("product", productRepository.findById(id).get());
		model.addAttribute("employees", employeeRepository.findAll());
		model.addAttribute("operation", poRepository.findById(oid).get());
		
		return "newProductivity";
	}
	@PostMapping("/addNewProductivity/{id}/{oid}")
	public String finishNewProductivitye(@PathVariable("id") Integer id,@PathVariable("oid") Integer oid,@ModelAttribute("productivity") Productivity productivity) {
		
		prodServices.saveNewProductivity(id,oid, productivity);
		
		return "redirect:/productOperationsValues/"+id;
	}
	
	@GetMapping("/thisMonthSalary/{id}")
	public String getThisMonthSalaryByEmployee(Model model ,@PathVariable("id") Integer id) {
		
	Salary salary = salaryRepository.findByEmployeeAndMonthAndYear(employeeRepository.findById(id).get(), LocalDate.now().getMonth(),LocalDate.now().getYear());
	List<Productivity> sorted = salary.getProductivities();
	sorted.sort(Comparator.comparing(Productivity::getProductivityDate));
	Map< LocalDate,Double> surveyMap = new LinkedHashMap<>();
	for (Productivity productivity : sorted) {
		if (surveyMap.containsKey(productivity.getProductivityDate())) {
			surveyMap.put(productivity.getProductivityDate(),surveyMap.get(productivity.getProductivityDate())+productivity.getProductivitySum());
		}else {
		surveyMap.put(productivity.getProductivityDate(),productivity.getProductivitySum());
		}
	}
	
	Salary previousSalary = salaryRepository.findByEmployeeAndMonthAndYear(employeeRepository.findById(id).get(), LocalDate.now().getMonth().minus(1),LocalDate.now().getYear());
	List<Productivity> previousSorted = previousSalary.getProductivities();
	previousSorted.sort(Comparator.comparing(Productivity::getProductivityDate));
	Map< LocalDate,Double> surveyMap3 = new LinkedHashMap<>();
	for (Productivity productivity : previousSorted) {
		if (surveyMap3.containsKey(productivity.getProductivityDate())) {
			surveyMap3.put(productivity.getProductivityDate(),surveyMap3.get(productivity.getProductivityDate())+productivity.getProductivitySum());
		}else {
		surveyMap3.put(productivity.getProductivityDate(),productivity.getProductivitySum());
		}
	}
	
	List<Salary> threeSalaries = salaryRepository.findByEmployeeAndMonthBetweenOrderByMonthAsc(employeeRepository.findById(id).get(), LocalDate.now().getMonth().minus(4), LocalDate.now().getMonth().minus(1));
	
	Map< Month,Double> surveyMap4 = new LinkedHashMap<>();
	for (Salary salary3 : threeSalaries) {
		surveyMap4.put(salary3.getMonth(), salary3.getSalary());
	}
	
	
	List<Salary> salaries = salaryRepository.findByEmployeeOrderByMonthDesc(employeeRepository.findById(id).get());
	salaries.sort(Comparator.comparing(Salary::getMonth));
	Map< Month,Double> surveyMap2 = new LinkedHashMap<>();
	for (Salary salary3 : salaries) {
		surveyMap2.put(salary3.getMonth(), salary3.getSalary());
	}
	
	String fullName = salary.getEmployee().getLastName() + " " + salary.getEmployee().getFirstName();
	model.addAttribute("fullName", fullName);
	model.addAttribute("surveyMap", surveyMap);
	model.addAttribute("surveyMap2", surveyMap2);
	model.addAttribute("surveyMap3", surveyMap3);
	model.addAttribute("surveyMap4", surveyMap4);
		return "bars";
	}
	
	@GetMapping("/allSalariesByEmployee/{id}")
	public String returnAllSalariesByEmployee(Model model,@PathVariable("id") Integer id) {
		
		List<Salary> salaries = salaryRepository.findByEmployeeOrderByMonthDesc(employeeRepository.findById(id).get());
		salaries.sort(Comparator.comparing(Salary::getMonth));
		Map< Month,Double> surveyMap2 = new LinkedHashMap<>();
		for (Salary salary : salaries) {
			surveyMap2.put(salary.getMonth(), salary.getSalary());
		}
		String fullName = employeeRepository.findById(id).get().getLastName() + " " + employeeRepository.findById(id).get().getFirstName();
		model.addAttribute("surveyMap2", surveyMap2);
		model.addAttribute("fullName", fullName);
		return "monthsBar";
		
	}
	
}
