package com.example.primavista.productivity.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import com.example.primavista.productivity.entity.Employee;
import com.example.primavista.productivity.repository.EmployeeRepository;
import com.example.primavista.productivity.services.ProductivityServices;

@Controller
public class EmployeeController {
	
	@Autowired
	EmployeeRepository employeeRepository;
	
	@Autowired
	ProductivityServices prodServices;
	
	@GetMapping("/createNewEmployee")
	public String addNewEmployeeForm(Model model) {
		
		model.addAttribute("employee", new Employee());
		
		return "newEmployee";
	}
	
	@PostMapping("createNewEmployee")
	public String addNewEmployee(@ModelAttribute("employee")Employee employee) {
		
		employeeRepository.save(employee);
		
		return "redirect:/";
	}
	
	@GetMapping("/employees")
	public String viewProductPage(Model model,@Param("search")String search) {
		
		 findPage(1,"lastName", "asc", model, search);
		 
		     return "allEmployees";
	}
	
	@GetMapping("/pages/{pageNumber}")
	public String findPage(@PathVariable("pageNumber") Integer pageNumber,
			@RequestParam("sortField") String sortField,
			@RequestParam("sortDir") String sortDir,Model model,
			@Param("search") String search) {
		
		Integer pageSize = 2;
		
		Page<Employee> pages = prodServices.findPagina(pageNumber, pageSize, sortField, sortDir, search);
		
		List<Employee> employees = pages.getContent();
		
		   model.addAttribute("currentPage",pageNumber);
		   model.addAttribute("totalPages", pages.getTotalPages());
		   model.addAttribute("totalItems", pages.getTotalElements());
		   model.addAttribute("sortField", sortField);
		   model.addAttribute("sortDir", sortDir);
		   model.addAttribute("search", search);
		   model.addAttribute("reverseSortDir", sortDir.equals("asc") ? "desc" : "asc");
		   model.addAttribute("employees", employees);
		
		     return "allEmployees";
		}
	
	@GetMapping("/updateEmployee/{id}")
	public String updateEmployeePage(Model model,@PathVariable("id")Integer id) {
		
		model.addAttribute("employee", employeeRepository.findById(id).get());
		
		return "newEmployee";
	}
	
	@PostMapping("/updateEmployee/{id}")
	public String updateEmployee(@PathVariable("id")Integer id,@ModelAttribute("employee")Employee employee) {
		
		Employee employee1 = employeeRepository.findById(id).get();
		employee1.setAddress(employee.getAddress());
		employee1.setBankAccount(employee.getBankAccount());
		employee1.setBday(employee.getBday());
		employee1.setEmdr(employee.getEmdr());
		employee1.setEmployedFrom(employee.getEmployedFrom());
		employee1.setFirstName(employee.getFirstName());
		employee1.setLastName(employee.getLastName());
		employee1.setPhoneNumber(employee.getPhoneNumber());
		employeeRepository.save(employee1);
		
		return "redirect:/employees";
	}
	
    @GetMapping("/deleteEmployee/{id}")
	public  String deleteEmployee(@PathVariable("id")Integer id) {
		
		Employee employee = employeeRepository.findById(id).get();
       // TUKA TREBA DA IMA KOD ZA DA SE IZBRISAT POVRZANI RABOTI
		employeeRepository.delete(employee);
		return "redirect:/employees";
		
	}
}
