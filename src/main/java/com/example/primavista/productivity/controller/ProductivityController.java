package com.example.primavista.productivity.controller;

import java.util.ArrayList;
import java.util.List;
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
import com.example.primavista.productivity.repository.EmployeeRepository;
import com.example.primavista.productivity.repository.ProductivityRepository;
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
	
	
	
	
}
