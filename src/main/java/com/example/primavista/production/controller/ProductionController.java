package com.example.primavista.production.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.example.primavista.documents.repository.CompanyRepository;
import com.example.primavista.production.entity.Product;
import com.example.primavista.production.repository.CutRepository;
import com.example.primavista.production.repository.LotRepository;
import com.example.primavista.production.repository.OperationRepository;
import com.example.primavista.production.repository.ProductRepository;
import com.example.primavista.production.services.ProductionServices;

@Controller
public class ProductionController {
	
	@Autowired
	ProductionServices proSevices;
	
	@Autowired
	ProductRepository productRepository;
	
	@Autowired
	OperationRepository operationRepository;
	
	@Autowired
	CutRepository cutRepository;
	
	@Autowired
	LotRepository lotRepository;
	
	@Autowired
	CompanyRepository companyRepository;
	
	@GetMapping("/newProduct")
	public String newProductForm(Model model) {
		
		model.addAttribute("product", new Product());
		model.addAttribute("operations", operationRepository.findAll());
		model.addAttribute("companies", companyRepository.findAll());
		return "newProductForm";
	}
	
	@PostMapping("/newProduct")
	public String createNewProduct(@ModelAttribute("product")Product product) {
		
		proSevices.saveNewProduct(product);
		return "redirect:/";
		
	}
	
	@GetMapping("/allProducts")
	public String getAllProducts(Model model) {
		
		model.addAttribute("products", productRepository.findAll());
		
		return "allProducts";
	}

}
