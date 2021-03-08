package com.example.primavista.documents.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.multipart.MultipartFile;

import com.example.primavista.documents.entity.Company;
import com.example.primavista.documents.entity.Invoice;
import com.example.primavista.documents.entity.InvoiceSlip;
import com.example.primavista.documents.repository.CompanyRepository;
import com.example.primavista.documents.repository.InvoiceSlipRepository;
import com.example.primavista.documents.services.DocumentsServices;

@Controller
public class DocumentsController {
	
	@Autowired
	DocumentsServices docServices;
	
	@Autowired
	CompanyRepository companyRepository;
	
	@Autowired
	InvoiceSlipRepository isRepository;

	@GetMapping("/")
	public String splashPage(Model model) {
		
		
		
		return "index";
	}
	
	@GetMapping("/createNewCompany")
	public String getNewCompanyForm(Model model) {
		
		model.addAttribute("company", new Company());
		
		return "newCompanyForm";
		
	}
	
	@PostMapping("/createNewCompany")
	public String saveNewCompany(@ModelAttribute("company") Company company) {
		
		docServices.saveNewCompany(company);
		
		return "redirect:/";
		
	}
	
	@GetMapping("/allCompanies")
	public String findAllCompanies(Model model) {
		
		model.addAttribute("companies", companyRepository.findAll());
		
		return "allCompanies";
		
	}
	
	@GetMapping("/newSlip/{id}")
	public String newInvoiceSlip(Model model,@PathVariable("id") Integer id,MultipartFile file) {
		
		model.addAttribute("company", companyRepository.findById(id).get());
		model.addAttribute("slip", new InvoiceSlip());
		model.addAttribute("file", file);
		return "newSlip";
	}
	
	@PostMapping("/newSlip/{id}")
	public String saveNewInvoiceSlip(@ModelAttribute("slip")InvoiceSlip slip,@PathVariable("id") Integer id,MultipartFile file ) {
		
		docServices.saveNewInvoiceSlip(slip, id, file);
		
		return "redirect:/";
		
	}
	
	@GetMapping("/allSlips")
	public String getAllSlips(Model model) {
		
		model.addAttribute("slips", isRepository.findAll());
		
		return "allSlips";
	}
	
	@GetMapping("/allunpaidSlips/{id}")
	public String getAllByCompanyWithoutInvoice(Model model,@PathVariable("id") Integer id ) {
		Company company = companyRepository.findById(id).get();
		model.addAttribute("slips", isRepository.findByCompanyAndInvoice(company, null));
		
		return "allSlips";
	}
	
	@GetMapping("/createInvoice/{id}")
	public String getNewInvoiceForm(Model model,@PathVariable("id") Integer id,MultipartFile file ) {
		
		Company company = companyRepository.findById(id).get();
		model.addAttribute("company", company);
		model.addAttribute("invoice", new Invoice());
		model.addAttribute("file", file);
		model.addAttribute("slipss", isRepository.findByCompanyAndInvoice(company, null));
		
		return "newInvoice";
		
	}
	
	@PostMapping("/createInvoice/{id}")
	public String createNewInvoice(@PathVariable("id") Integer id,MultipartFile file,@ModelAttribute("invoice")Invoice invoice, @Param("sid")Integer sid ) {
		
		docServices.createNewInvoice(invoice, file, id,sid);
		
		return "redirect:/";
		
	}
	
	
	
}
