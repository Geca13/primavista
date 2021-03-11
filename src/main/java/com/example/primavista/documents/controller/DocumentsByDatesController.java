package com.example.primavista.documents.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.example.primavista.documents.entity.BillReceipt;
import com.example.primavista.documents.entity.Company;
import com.example.primavista.documents.repository.BillReceiptRepository;
import com.example.primavista.documents.repository.CompanyRepository;
import com.example.primavista.documents.repository.CorrespondenceRepository;
import com.example.primavista.documents.repository.InstitutionRepository;
import com.example.primavista.documents.repository.InvoiceRepository;
import com.example.primavista.documents.repository.InvoiceSlipRepository;
import com.example.primavista.documents.services.DocumentsServices;

@Controller
public class DocumentsByDatesController {
	
	@Autowired
	DocumentsServices docServices;
	
	@Autowired
	CompanyRepository companyRepository;
	
	@Autowired
	InvoiceSlipRepository isRepository;
	
	@Autowired
	InvoiceRepository invoiceRepository;
	
	@Autowired
    BillReceiptRepository receiptRepository;
	
	@Autowired
	InstitutionRepository institutionRepository;
	
	@Autowired
	CorrespondenceRepository corRepository;
	
	@GetMapping("/receiptsBetweenDates")
	public String findBetweenDates(Model model,@Param(value="id")String id,@Param(value="startDate") String startDate,@Param(value="endDate") String endDate ){
		
		if(id.equals("")&& startDate.isEmpty() && endDate.isEmpty()) {
			return "redirect:/allReceipts?empty";
		}
		
		 if(id.equals("")) {
			 LocalDate d1 = LocalDate.parse(startDate);
		     LocalDate d2 = LocalDate.parse(endDate);
			 List<BillReceipt> list = receiptRepository.findByDateBetween(d1, d2);
			 model.addAttribute("receipts", list);
			 model.addAttribute("companies", companyRepository.findAll());
			 return "allReceiptsPage";
		 }
		 if(startDate.isEmpty() && endDate.isEmpty()) {
			 Company company = companyRepository.findById(Integer.valueOf(id)).get();
			 List<BillReceipt> list = receiptRepository.findByCompany(company);
			 model.addAttribute("receipts", list);
			 model.addAttribute("companies", companyRepository.findAll());
			 return "allReceiptsPage";
		 }
		 LocalDate d1 = LocalDate.parse(startDate);
	     LocalDate d2 = LocalDate.parse(endDate);
		 Company company = companyRepository.findById(Integer.valueOf(id)).get();
		 List<BillReceipt> list = receiptRepository.findByCompanyAndDateBetween(company,d1, d2);
		 model.addAttribute("receipts", list);
		 model.addAttribute("companies", companyRepository.findAll());
		 return "allReceiptsPage";
		 
	 }

}
