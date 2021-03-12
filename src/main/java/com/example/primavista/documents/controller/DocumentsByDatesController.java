package com.example.primavista.documents.controller;

import java.time.LocalDate;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import com.example.primavista.documents.entity.BillReceipt;
import com.example.primavista.documents.entity.Invoice;
import com.example.primavista.documents.entity.InvoiceSlip;
import com.example.primavista.documents.repository.BillReceiptRepository;
import com.example.primavista.documents.repository.CompanyRepository;
import com.example.primavista.documents.repository.CorrespondenceRepository;
import com.example.primavista.documents.repository.InstitutionRepository;
import com.example.primavista.documents.repository.InvoiceRepository;
import com.example.primavista.documents.repository.InvoiceSlipRepository;


@Controller
public class DocumentsByDatesController {
	
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
			
			List<BillReceipt> list = receiptRepository.findByCompanyAndDateBetween(companyRepository.findById(Integer.valueOf(id)).get(),receiptRepository.findAllByOrderByDateAsc().get(0).getDate(), LocalDate.now());
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
			
		     List<BillReceipt> list = receiptRepository.findByDateBetween(LocalDate.parse(startDate), LocalDate.parse(endDate));
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
			
		     List<BillReceipt> list = receiptRepository.findByDateBetween(receiptRepository.findAllByOrderByDateAsc().get(0).getDate(), LocalDate.parse(endDate));
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
			 
		     List<BillReceipt> list = receiptRepository.findByDateBetween(LocalDate.parse(startDate), LocalDate.now());
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
			
		     List<BillReceipt> list = receiptRepository.findByCompanyAndDateBetween(companyRepository.findById(Integer.valueOf(id)).get(),LocalDate.parse(startDate), LocalDate.now());
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
			 
			 List<BillReceipt> list = receiptRepository.findByCompanyAndDateBetween(companyRepository.findById(Integer.valueOf(id)).get(),receiptRepository.findAllByOrderByDateAsc().get(0).getDate(), LocalDate.parse(endDate));
			 for (BillReceipt billReceipt : list) {
					totalOnReceipts = totalOnReceipts + billReceipt.getSum();
					model.addAttribute("totalOnReceipts", totalOnReceipts);
				}
			 model.addAttribute("receipts", list);
			 model.addAttribute("companies", companyRepository.findAll());
			 return "allReceiptsPage";
		 }
		 //rezultati so site parametri PROVERENO
		 List<BillReceipt> list = receiptRepository.findByCompanyAndDateBetween(companyRepository.findById(Integer.valueOf(id)).get(),LocalDate.parse(startDate), LocalDate.parse(endDate));
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
			
		   List<Invoice> list = invoiceRepository.findByCompanyAndIssuedBetween(companyRepository.findById(Integer.valueOf(id)).get(),invoiceRepository.findAllByOrderByIssuedAsc().get(0).getIssued(), LocalDate.now());
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
			 
		    List<Invoice> list = invoiceRepository.findByIssuedBetween(LocalDate.parse(startDate), LocalDate.parse(endDate));
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
			
		    List<Invoice> list = invoiceRepository.findByIssuedBetween(invoiceRepository.findAllByOrderByIssuedAsc().get(0).getIssued(), LocalDate.parse(endDate));
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
			
		     List<Invoice> list = invoiceRepository.findByIssuedBetween(LocalDate.parse(startDate), LocalDate.now());
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
			
		    List<Invoice> list = invoiceRepository.findByCompanyAndIssuedBetween(companyRepository.findById(Integer.valueOf(id)).get(),LocalDate.parse(startDate), LocalDate.now());
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
			 
			 List<Invoice> list = invoiceRepository.findByCompanyAndIssuedBetween(companyRepository.findById(Integer.valueOf(id)).get(),invoiceRepository.findAllByOrderByIssuedAsc().get(0).getIssued(), LocalDate.parse(endDate));
			 for (Invoice billReceipt : list) {
					totalOnReceipts = totalOnReceipts + billReceipt.getSum();
					model.addAttribute("totalOnReceipts", totalOnReceipts);
				}
			 model.addAttribute("invoices", list);
			 model.addAttribute("companies", companyRepository.findAll());
			 return "allInvoices";
		 }
		 //rezultati so site parametri PROVERENO
		 List<Invoice> list = invoiceRepository.findByCompanyAndIssuedBetween(companyRepository.findById(Integer.valueOf(id)).get(),LocalDate.parse(startDate), LocalDate.parse(endDate));
		 for (Invoice billReceipt : list) {
				totalOnReceipts = totalOnReceipts + billReceipt.getSum();
				model.addAttribute("totalOnReceipts", totalOnReceipts);
			}
		 model.addAttribute("invoices", list);
		 model.addAttribute("companies", companyRepository.findAll());
		 return "allInvoices";
	}
	
	@GetMapping("/invoiceSlipsBetweenDates")
	public String findInvoiceSlipsBetweenDates(Model model,@Param(value="id")String id,@Param(value="startDate") String startDate,@Param(value="endDate") String endDate ){
		
		if(id.equals("")&& startDate.isEmpty() && endDate.isEmpty()) {
			return "redirect:/allSlips?empty";
		}
		//site vrati kompanija PROVERENO
		if(!id.equals("")&& startDate.isEmpty() && endDate.isEmpty()) {
			
			List<InvoiceSlip> list = isRepository.findByCompanyAndIssuedBetween(companyRepository.findById(Integer.valueOf(id)).get(),isRepository.findAllByOrderByIssuedAsc().get(0).getIssued(), LocalDate.now());
			model.addAttribute("slips", list);
			model.addAttribute("companies", companyRepository.findAll());
		    return "allSlips";
		}
		//site vrati gi od -do PROVERENO
		 if(id.equals("") && !startDate.isEmpty() && !endDate.isEmpty()) {
		    
			 List<InvoiceSlip> list = isRepository.findByIssuedBetween(LocalDate.parse(startDate), LocalDate.parse(endDate));
			 model.addAttribute("slips", list);
			 model.addAttribute("companies", companyRepository.findAll());
			 return "allSlips";
		 }
		 //site od pocetok do opredelena data PROVERENO
		 if(id.equals("") && startDate.isEmpty() && !endDate.isEmpty()) {
			 
             List<InvoiceSlip> list = isRepository.findByIssuedBetween(isRepository.findAllByOrderByIssuedAsc().get(0).getIssued(), LocalDate.parse(endDate));
			 model.addAttribute("slips", list);
			 model.addAttribute("companies", companyRepository.findAll());
			 return "allSlips";
		 }
		 //site od data do denes PROVERENO
		 if(id.equals("") && !startDate.isEmpty() && endDate.isEmpty()) {
		    
			 List<InvoiceSlip> list = isRepository.findByIssuedBetween(LocalDate.parse(startDate), LocalDate.now());
			 model.addAttribute("slips", list);
			 model.addAttribute("companies", companyRepository.findAll());
			 return "allSlips";
		 }
		 //site po kompanija od data do denes PROVERENO
		 if(!id.equals("") && !startDate.isEmpty() && endDate.isEmpty()) {
			 List<InvoiceSlip> list = isRepository.findByCompanyAndIssuedBetween(companyRepository.findById(Integer.valueOf(id)).get(),LocalDate.parse(startDate), LocalDate.now());
			 model.addAttribute("slips", list);
			 model.addAttribute("companies", companyRepository.findAll());
			 return "allSlips";
		 }
		 //site po kompanija od pocetna data do opredelena data PROVERENO
		 if(!id.equals("") && startDate.isEmpty() && !endDate.isEmpty()) {
			 List<InvoiceSlip> list = isRepository.findByCompanyAndIssuedBetween(companyRepository.findById(Integer.valueOf(id)).get(),isRepository.findAllByOrderByIssuedAsc().get(0).getIssued(), LocalDate.parse(endDate));
			 model.addAttribute("slips", list);
			 model.addAttribute("companies", companyRepository.findAll());
			 return "allSlips";
		 }
		 //rezultati so site parametri PROVERENO
		 List<InvoiceSlip> list = isRepository.findByCompanyAndIssuedBetween(companyRepository.findById(Integer.valueOf(id)).get(),LocalDate.parse(startDate), LocalDate.parse(endDate));
		 model.addAttribute("slips", list);
		 model.addAttribute("companies", companyRepository.findAll());
		 return "allSlips";
	}
	
	
}
