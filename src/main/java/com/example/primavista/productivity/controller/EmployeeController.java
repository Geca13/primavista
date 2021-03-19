package com.example.primavista.productivity.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.example.primavista.productivity.entity.Employee;
import com.example.primavista.productivity.repository.EmployeeRepository;

@Controller
public class EmployeeController {
	
	@Autowired
	EmployeeRepository employeeRepository;
	
	@GetMapping("createNewEmployee")
	public String addNewEmployeeForm(Model model) {
		
		model.addAttribute("employee", new Employee());
		
		return "newEmployee";
	}
	
	@PostMapping("createNewEmployee")
	public String addNewEmployee(@ModelAttribute("employee")Employee employee) {
		
		employeeRepository.save(employee);
		
		return "redirect:/";
	}
	
	@GetMapping("allEmployees")
	public String allEmployeesList(Model model) {
		
		model.addAttribute("employees", employeeRepository.findAll());
		
		return "allEmployeesTable";
	}
	

}
