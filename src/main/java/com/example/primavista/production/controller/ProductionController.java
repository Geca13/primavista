package com.example.primavista.production.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.example.primavista.documents.repository.CompanyRepository;
import com.example.primavista.production.entity.Cut;
import com.example.primavista.production.entity.Lot;
import com.example.primavista.production.entity.Product;
import com.example.primavista.production.entity.Size;
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
	
	@GetMapping("/newCut/{id}")
	public String createCutForm(Model model,@PathVariable("id")Integer id) {
		model.addAttribute("product",productRepository.findById(id).get());
		model.addAttribute("products", productRepository.findAll());
		model.addAttribute("cut", new Cut());
		return "newCutForm";
		
	}
	
	@PostMapping("/newCut/{id}")
	public String createCut(@ModelAttribute("cut")Cut cut,@PathVariable("id")Integer id) {
		Product product = productRepository.findById(id).get();
		Cut newCut = new Cut();
		
		newCut.setProduct(product);
		newCut.setNumberOfLots(cut.getNumberOfLots());
		newCut.setNumberOfSheets(cut.getNumberOfSheets());
		cutRepository.save(newCut);
		for (int i = 1; i <= newCut.getNumberOfLots(); i++) {
			Lot lot =new Lot();
			lot.setQty(newCut.getNumberOfSheets());
			lot.setProduct(newCut.getProduct());
			lot.setCut(newCut);
			List<Lot> lots = lotRepository.findAllByProduct(newCut.getProduct());
			lot.setLid(lots.size()+1);
			lotRepository.save(lot);
		}
		
		return "redirect:/addSizes/"+newCut.getId();
		
	}
	
	@GetMapping("/addSizes/{id}")
	
	public String lotSizesForm(Model model,@PathVariable("id")Integer id) {
		Cut cut = cutRepository.findById(id).get();
		model.addAttribute("cut",cut);
		List<Lot> lots = lotRepository.findAllByCut(cut);
		for (Lot lot : lots) {
			model.addAttribute("lot",lot);
		}
		model.addAttribute("lots",lots);
		
		
		return "sizesForm";
		
	}
	
   @PostMapping("/addSizes/{id}")
	
	public String addSizesForm(Model model,@PathVariable("id")Integer id,@ModelAttribute("lot")Lot lot) {
	Cut cut = cutRepository.findById(id).get();
	List<Lot>lots = lotRepository.findAllByCut(cut);
	for (Lot lot1 : lots) {
		lot1.setSize(lot.getSize());
		lotRepository.save(lot1);
	}
		
		return "redirect:/";
	}
	
	

}
