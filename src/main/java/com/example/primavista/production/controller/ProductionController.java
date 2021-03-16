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
import com.example.primavista.production.entity.Operation;
import com.example.primavista.production.entity.Product;
import com.example.primavista.production.entity.ProductOpers;
import com.example.primavista.production.entity.Size;
import com.example.primavista.production.repository.CutRepository;
import com.example.primavista.production.repository.LotRepository;
import com.example.primavista.production.repository.OperationRepository;
import com.example.primavista.production.repository.ProductOperationsRepository;
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
	
	@Autowired
	ProductOperationsRepository poRepository;
	
	@GetMapping("/newProduct")
	public String newProductForm(Model model) {
		
		model.addAttribute("product", new Product());
		model.addAttribute("operations", operationRepository.findAll());
		model.addAttribute("companies", companyRepository.findAll());
		return "newProductForm";
	}
	
	@PostMapping("/newProduct")
	public String createNewProduct(@ModelAttribute("product")Product product) {
		Product newProduct = new Product();
		newProduct.setCompany(product.getCompany());
		newProduct.setDescription(product.getDescription());
		newProduct.setOperations(product.getOperations());
		newProduct.setCompleted(false);
		productRepository.save(newProduct);
		for (Operation operation : newProduct.getOperations()) {
			ProductOpers oper = new ProductOpers();
			oper.setOperation(operation);
			oper.setProduct(newProduct);
			poRepository.save(oper);
		}
		return "redirect:/addValues/"+newProduct.getId();
	}
	
	
	@GetMapping("/addValues/{id}")
	public String operationsValuesForm(Model model,@PathVariable("id")Integer id) {
		Product product = productRepository.findById(id).get();
		model.addAttribute("product",product);
		List<ProductOpers> operations = poRepository.findAllByProduct(product);
		for (ProductOpers operation : operations) {
			model.addAttribute("operation",operation);
		}
		model.addAttribute("operations",operations);
		
	    return "setValuesOnOperationsPage";
	}
	
   @PostMapping("/addValues/{id}")
	public String addValuesForm(Model model,@PathVariable("id")Integer id,@ModelAttribute("operation")ProductOpers operation) {
	   Product product = productRepository.findById(id).get();
	   List<ProductOpers> operations = poRepository.findAllByProductAndOperationValue(product, null);
	
	   for (ProductOpers operation1 : operations) {
		   operation1.setOperationValue(operation.getOperationValue());
		   poRepository.save(operation1);
		if(operations.indexOf(operation1) == operations.size()-1) {
			return "redirect:/";
		}
		return "redirect:/addValues/"+product.getId();
	}
	    return "redirect:/";
	}
   
   
	
	@GetMapping("/allProducts")
	public String getAllProducts(Model model) {
		List<Product> products = productRepository.findAll();
		model.addAttribute("products", products);
		for (Product product : products) {
			List<ProductOpers> operations = poRepository.findAllByProductId(product.getId());
			model.addAttribute("operations", operations);
		}
		
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
	public String addSizesForm(Model model,@PathVariable("id")Integer id,@ModelAttribute("lot")Lot lot,Size size) {
	Cut cut = cutRepository.findById(id).get();
	List<Lot>lots = lotRepository.findAllByCutAndSize(cut, null);
	
		for (Lot lot1 : lots) {
		lot1.setSize(lot.getSize());
		lotRepository.save(lot1);
		if(lots.indexOf(lot1) == lots.size()-1) {
			return "redirect:/";
		}
		return "redirect:/addSizes/"+cut.getId();
	}
	
		return "redirect:/";
	}
   
   @GetMapping("/cutsByProduct/{id}")
   public String returnAllCutsByProduct(Model model,@PathVariable("id")Integer id) {
	   model.addAttribute("product", productRepository.findById(id).get());
	   model.addAttribute("cuts", cutRepository.findAllByProductId(id));
	   return "cuts";
   }
   
   @GetMapping("/updateCut/{id}")
   public String getCutsUpdateForm(Model model, @PathVariable("id")Integer id) {
	  
	   model.addAttribute("cut", cutRepository.findById(id).get());
	   model.addAttribute("products", productRepository.findAll());
	   
	return "cutsUpdateForm";
   }
   
   @GetMapping("/deleteCut/{id}")
   public String deleteCut(Model model, @PathVariable("id")Integer id) {
	  
	  Cut cut = cutRepository.findById(id).get();
	   List<Lot> lots = lotRepository.findAllByCut(cut);
	   for (Lot lot : lots) {
		lotRepository.delete(lot);
	}
	   Product product = cut.getProduct();
	   cutRepository.delete(cut);
			   
	return "redirect:/cutsByProduct/"+product.getId();
   }
   
   @PostMapping("/updateCut/{id}")
   public String updateCut(@ModelAttribute("cut")Cut cut, @PathVariable("id")Integer id) {
	   
	   Cut cutForUpdate = cutRepository.findById(id).get();
	   cutForUpdate.setNumberOfSheets(cut.getNumberOfSheets());
	   cutForUpdate.setProduct(cut.getProduct());
	   cutRepository.save(cutForUpdate);
	   List<Lot> lots = lotRepository.findAllByCut(cutForUpdate);
	   for (Lot lot : lots) {
		lot.setProduct(cutForUpdate.getProduct());
		lot.setQty(cutForUpdate.getNumberOfSheets());
		lotRepository.save(lot);
		
	}
	   Product product = cutForUpdate.getProduct();
	return "redirect:/cutsByProduct/"+product.getId();
	}
   
   @GetMapping("/lotsByProduct/{id}")
   public String returnAllLotsByProduct(Model model,@PathVariable("id")Integer id) {
	   model.addAttribute("product", productRepository.findById(id).get());
	   model.addAttribute("lots", lotRepository.findAllByProductId(id));
	   return "allLots";
   }
   
   @GetMapping("/lotById/{id}")
   public String returnLotByIdForm(Model model,@PathVariable("id")Integer id) {
	   model.addAttribute("lot", lotRepository.findById(id).get());
	   
	   return "lotUpdateForm";
   }
   
   @PostMapping("/lotById/{id}")
   public String saveLotByIdForm(Model model,@PathVariable("id")Integer id,@ModelAttribute("lot") Lot lot) {
	   
	   Lot lot1 = lotRepository.findById(id).get();
	   lot1.setQty(lot.getQty());
	   lot1.setSize(lot.getSize());
	   lotRepository.save(lot1);
	   Product product = lot1.getProduct();
	   return "redirect:/lotsByProduct/"+product.getId();
   }
   
   @GetMapping("/deleteLot/{id}")
   public String deleteLotById(Model model,@PathVariable("id")Integer id) {
	   
	   Lot lot = lotRepository.findById(id).get();
	   Cut cut = lot.getCut();
	   cut.setNumberOfLots(cut.getNumberOfLots()-1);
	   cutRepository.save(cut);
	   lotRepository.delete(lot);
	   if(cut.getNumberOfLots()==0) {
		   cutRepository.delete(cut);
	   }
	   return "redirect:/lotsByProduct/"+lot.getProduct().getId();
   }
   
	
	

}
