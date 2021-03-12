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
import com.example.primavista.documents.entity.Invoice;
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
		Double totalOnReceipts= 0.00;
		if(id.equals("")&& startDate.isEmpty() && endDate.isEmpty()) {
			return "redirect:/allReceipts?empty";
		}
		
		//site vrati kompanija PROVERENO
		if(!id.equals("")&& startDate.isEmpty() && endDate.isEmpty()) {
			
				 List<BillReceipt> forId =receiptRepository.findAllByOrderByDateAsc();
				 LocalDate d1 = forId.get(0).getDate();
				 LocalDate d2 = LocalDate.now();
			     Company company = companyRepository.findById(Integer.valueOf(id)).get();
				 List<BillReceipt> list = receiptRepository.findByCompanyAndDateBetween(company,d1, d2);
				 for (BillReceipt billReceipt : list) {
						totalOnReceipts = totalOnReceipts + billReceipt.getSum();
						model.addAttribute("totalOnReceipts", totalOnReceipts);
					}
				 model.addAttribute("receipts", list);
				 model.addAttribute("companies", companyRepository.findAll());
				 return "allReceiptsPage";
			 
		}
		
		//site vrati gi od -do PROVERENO
		 if(id.equals("") && !startDate.isEmpty() && !endDate.isEmpty()) {
			 LocalDate d1 = LocalDate.parse(startDate);
		     LocalDate d2 = LocalDate.parse(endDate);
			 List<BillReceipt> list = receiptRepository.findByDateBetween(d1, d2);
			 for (BillReceipt billReceipt : list) {
				totalOnReceipts = totalOnReceipts + billReceipt.getSum();
				model.addAttribute("totalOnReceipts", totalOnReceipts);
			}
			 model.addAttribute("receipts", list);
			 model.addAttribute("companies", companyRepository.findAll());
			 return "allReceiptsPage";
		 }
		 //site od pocetok do opredelena data PROVERENO
		 if(id.equals("") && startDate.isEmpty() && !endDate.isEmpty()) {
			 LocalDate d1 = receiptRepository.findAllByOrderByDateAsc().get(0).getDate();
		     LocalDate d2 = LocalDate.parse(endDate);
			 List<BillReceipt> list = receiptRepository.findByDateBetween(d1, d2);
			 for (BillReceipt billReceipt : list) {
					totalOnReceipts = totalOnReceipts + billReceipt.getSum();
					model.addAttribute("totalOnReceipts", totalOnReceipts);
				}
			 model.addAttribute("receipts", list);
			 model.addAttribute("companies", companyRepository.findAll());
			 return "allReceiptsPage";
		 }
		 
		//site od data do denes PROVERENO
		 if(id.equals("") && !startDate.isEmpty() && endDate.isEmpty()) {
			 LocalDate d1 = LocalDate.parse(startDate);
		     LocalDate d2 = LocalDate.now();
			 List<BillReceipt> list = receiptRepository.findByDateBetween(d1, d2);
			 for (BillReceipt billReceipt : list) {
					totalOnReceipts = totalOnReceipts + billReceipt.getSum();
					model.addAttribute("totalOnReceipts", totalOnReceipts);
				}
			 model.addAttribute("receipts", list);
			 model.addAttribute("companies", companyRepository.findAll());
			 return "allReceiptsPage";
		 }
		 
		//site po kompanija od data do denes PROVERENO
		 if(!id.equals("") && !startDate.isEmpty() && endDate.isEmpty()) {
			 LocalDate d1 = LocalDate.parse(startDate);
		     LocalDate d2 = LocalDate.now();
		     Company company = companyRepository.findById(Integer.valueOf(id)).get();
			 List<BillReceipt> list = receiptRepository.findByCompanyAndDateBetween(company,d1, d2);
			 for (BillReceipt billReceipt : list) {
					totalOnReceipts = totalOnReceipts + billReceipt.getSum();
					model.addAttribute("totalOnReceipts", totalOnReceipts);
				}
			 model.addAttribute("receipts", list);
			 model.addAttribute("companies", companyRepository.findAll());
			 return "allReceiptsPage";
		 }
		 
		//site po kompanija od pocetna data do opredelena data PROVERENO
		 if(!id.equals("") && startDate.isEmpty() && !endDate.isEmpty()) {
			 List<BillReceipt> forId =receiptRepository.findAllByOrderByDateAsc();
			 LocalDate d1 = forId.get(0).getDate();
			 LocalDate d2 = LocalDate.parse(endDate);
		     Company company = companyRepository.findById(Integer.valueOf(id)).get();
			 List<BillReceipt> list = receiptRepository.findByCompanyAndDateBetween(company,d1, d2);
			 for (BillReceipt billReceipt : list) {
					totalOnReceipts = totalOnReceipts + billReceipt.getSum();
					model.addAttribute("totalOnReceipts", totalOnReceipts);
				}
			 model.addAttribute("receipts", list);
			 model.addAttribute("companies", companyRepository.findAll());
			 return "allReceiptsPage";
		 }
		 //rezultati so site parametri PROVERENO
		 LocalDate d1 = LocalDate.parse(startDate);
	     LocalDate d2 = LocalDate.parse(endDate);
		 Company company = companyRepository.findById(Integer.valueOf(id)).get();
		 List<BillReceipt> list = receiptRepository.findByCompanyAndDateBetween(company,d1, d2);
		 for (BillReceipt billReceipt : list) {
				totalOnReceipts = totalOnReceipts + billReceipt.getSum();
				model.addAttribute("totalOnReceipts", totalOnReceipts);
			}
		 model.addAttribute("receipts", list);
		 model.addAttribute("companies", companyRepository.findAll());
		 return "allReceiptsPage";
	}
	
	
	
	
	@GetMapping("/invoicesBetweenDates")
	public String findInvoicesBetweenDates(Model model,@Param(value="id")String id,@Param(value="startDate") String startDate,@Param(value="endDate") String endDate ){
		Double totalOnReceipts= 0.00;
		if(id.equals("")&& startDate.isEmpty() && endDate.isEmpty()) {
			return "redirect:/invoices?empty";
		}
		
		//site vrati kompanija PROVERENO
		if(!id.equals("")&& startDate.isEmpty() && endDate.isEmpty()) {
			
				 List<Invoice> forId =invoiceRepository.findAllByOrderByIssuedAsc();
				 LocalDate d1 = forId.get(0).getIssued();
				 LocalDate d2 = LocalDate.now();
			     Company company = companyRepository.findById(Integer.valueOf(id)).get();
				 List<Invoice> list = invoiceRepository.findByCompanyAndIssuedBetween(company,d1, d2);
				 for (Invoice billReceipt : list) {
						totalOnReceipts = totalOnReceipts + billReceipt.getSum();
						model.addAttribute("totalOnReceipts", totalOnReceipts);
					}
				 model.addAttribute("invoices", list);
				 model.addAttribute("companies", companyRepository.findAll());
				 return "allInvoices";
			 
		}
		
		//site vrati gi od -do PROVERENO
		 if(id.equals("") && !startDate.isEmpty() && !endDate.isEmpty()) {
			 LocalDate d1 = LocalDate.parse(startDate);
		     LocalDate d2 = LocalDate.parse(endDate);
			 List<Invoice> list = invoiceRepository.findByIssuedBetween(d1, d2);
			 for (Invoice billReceipt : list) {
				totalOnReceipts = totalOnReceipts + billReceipt.getSum();
				model.addAttribute("totalOnReceipts", totalOnReceipts);
			}
			 model.addAttribute("invoices", list);
			 model.addAttribute("companies", companyRepository.findAll());
			 return "allInvoices";
		 }
		 //site od pocetok do opredelena data PROVERENO
		 if(id.equals("") && startDate.isEmpty() && !endDate.isEmpty()) {
			 LocalDate d1 = receiptRepository.findAllByOrderByDateAsc().get(0).getDate();
		     LocalDate d2 = LocalDate.parse(endDate);
			 List<Invoice> list = invoiceRepository.findByIssuedBetween(d1, d2);
			 for (Invoice billReceipt : list) {
					totalOnReceipts = totalOnReceipts + billReceipt.getSum();
					model.addAttribute("totalOnReceipts", totalOnReceipts);
				}
			 model.addAttribute("invoices", list);
			 model.addAttribute("companies", companyRepository.findAll());
			 return "allInvoices";
		 }
		 
		//site od data do denes PROVERENO
		 if(id.equals("") && !startDate.isEmpty() && endDate.isEmpty()) {
			 LocalDate d1 = LocalDate.parse(startDate);
		     LocalDate d2 = LocalDate.now();
			 List<Invoice> list = invoiceRepository.findByIssuedBetween(d1, d2);
			 for (Invoice billReceipt : list) {
					totalOnReceipts = totalOnReceipts + billReceipt.getSum();
					model.addAttribute("totalOnReceipts", totalOnReceipts);
				}
			 model.addAttribute("invoices", list);
			 model.addAttribute("companies", companyRepository.findAll());
			 return "allInvoices";
		 }
		 
		//site po kompanija od data do denes PROVERENO
		 if(!id.equals("") && !startDate.isEmpty() && endDate.isEmpty()) {
			 LocalDate d1 = LocalDate.parse(startDate);
		     LocalDate d2 = LocalDate.now();
		     Company company = companyRepository.findById(Integer.valueOf(id)).get();
			 List<Invoice> list = invoiceRepository.findByCompanyAndIssuedBetween(company,d1, d2);
			 for (Invoice billReceipt : list) {
					totalOnReceipts = totalOnReceipts + billReceipt.getSum();
					model.addAttribute("totalOnReceipts", totalOnReceipts);
				}
			 model.addAttribute("invoices", list);
			 model.addAttribute("companies", companyRepository.findAll());
			 return "allInvoices";
		 }
		 
		//site po kompanija od pocetna data do opredelena data PROVERENO
		 if(!id.equals("") && startDate.isEmpty() && !endDate.isEmpty()) {
			 List<Invoice> forId =invoiceRepository.findAllByOrderByIssuedAsc();
			 LocalDate d1 = forId.get(0).getIssued();
			 LocalDate d2 = LocalDate.parse(endDate);
		     Company company = companyRepository.findById(Integer.valueOf(id)).get();
			 List<Invoice> list = invoiceRepository.findByCompanyAndIssuedBetween(company,d1, d2);
			 for (Invoice billReceipt : list) {
					totalOnReceipts = totalOnReceipts + billReceipt.getSum();
					model.addAttribute("totalOnReceipts", totalOnReceipts);
				}
			 model.addAttribute("invoices", list);
			 model.addAttribute("companies", companyRepository.findAll());
			 return "allInvoices";
		 }
		 //rezultati so site parametri PROVERENO
		 LocalDate d1 = LocalDate.parse(startDate);
	     LocalDate d2 = LocalDate.parse(endDate);
		 Company company = companyRepository.findById(Integer.valueOf(id)).get();
		 List<Invoice> list = invoiceRepository.findByCompanyAndIssuedBetween(company,d1, d2);
		 for (Invoice billReceipt : list) {
				totalOnReceipts = totalOnReceipts + billReceipt.getSum();
				model.addAttribute("totalOnReceipts", totalOnReceipts);
			}
		 model.addAttribute("invoices", list);
		 model.addAttribute("companies", companyRepository.findAll());
		 return "allInvoices";
	}

}
