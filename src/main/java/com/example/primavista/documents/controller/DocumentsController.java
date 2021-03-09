package com.example.primavista.documents.controller;

import java.io.IOException;
import java.util.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.multipart.MultipartFile;
import com.example.primavista.documents.entity.Company;
import com.example.primavista.documents.entity.Invoice;
import com.example.primavista.documents.entity.InvoiceSlip;
import com.example.primavista.documents.entity.Type;
import com.example.primavista.documents.repository.CompanyRepository;
import com.example.primavista.documents.repository.InvoiceRepository;
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
	
	@Autowired
	InvoiceRepository invoiceRepository;

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
		Invoice invoice = invoiceRepository.findById(id).get();
		model.addAttribute("slips", isRepository.findByCompanyAndInvoice(invoice.getCompany(), null));
		model.addAttribute("invoice", invoice);
		return "allSlips";
	}
	
	@GetMapping("/addunpaidSlip/{id}/{sid}")
	public String addSlipsToInvoice(@PathVariable("id") Integer id,@PathVariable("sid")  Integer sid) {
		Invoice invoice = invoiceRepository.findById(id).get();
		InvoiceSlip slip = isRepository.findById(sid).get();
		invoice.getSlips().add(slip);
		slip.setInvoice(invoice);
		invoiceRepository.save(invoice);
		isRepository.save(slip);
		return "redirect:/allunpaidSlips/"+invoice.getId();
	}
	
	@GetMapping("/createInvoice/{id}")
	public String getNewInvoiceForm(Model model,@PathVariable("id") Integer id,MultipartFile file ) {
		
		Company company = companyRepository.findById(id).get();
		model.addAttribute("company", company);
		model.addAttribute("invoice", new Invoice());
		model.addAttribute("file", file);
		
		return "newInvoice";
	}
	
	@PostMapping("/createInvoice/{id}")
	public String createNewInvoice(@PathVariable("id") Integer id,MultipartFile file,@ModelAttribute("invoice")Invoice invoice ) {
		
		Company company = companyRepository.findById(id).get();
		Invoice newInvoice = new Invoice();
		newInvoice.setSum(invoice.getSum());
		newInvoice.setInvoiceNumber(invoice.getInvoiceNumber());
		newInvoice.setInvoiceType(invoice.getInvoiceType());
		newInvoice.setIssued(invoice.getIssued());
		newInvoice.setArrival(invoice.getArrival());
		newInvoice.setCompany(company);
		String fileName = StringUtils.cleanPath(file.getOriginalFilename());
	       if(fileName.contains("..")) {
	       	System.out.println("not a valid file");
	        }
		  try {
			  newInvoice.setInvoiceImage(Base64.getEncoder().encodeToString(file.getBytes()));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		  invoiceRepository.save(newInvoice);
		
		return "redirect:/allunpaidSlips/"+newInvoice.getId();
	}
	
	@GetMapping("/invoices")
	public String findAllInvoices(Model model) {
		
		model.addAttribute("invoices", invoiceRepository.findAll());
		
		return "allInvoices";
	}
	
	@GetMapping("/invoices/{id}")
	public String findAllInvoicesByCompany(Model model, @PathVariable("id") Integer id) {
		
		model.addAttribute("invoices", invoiceRepository.findByCompanyId(id));
		
		return "allInvoices";
	}
	
	@GetMapping("/invoicesByInType/{id}")
	public String findAllInvoicesByCompanyAndType(Model model, @PathVariable("id") Integer id) {
		
		model.addAttribute("invoices", invoiceRepository.findByCompanyIdAndInvoiceType(id,Type.INCOMMING ));
		
		return "allInvoices";
	}
	
	@GetMapping("/invoicesByOutType/{id}")
	public String findAllInvoicesByCompanyAndOutType(Model model, @PathVariable("id") Integer id) {
		
		model.addAttribute("invoices", invoiceRepository.findByCompanyIdAndInvoiceType(id,Type.OUTGOING ));
		
		return "allInvoices";
	}
	
	@GetMapping("/slipsByInType/{id}")
	public String findAllSlipsByCompanyAndType(Model model, @PathVariable("id") Integer id) {
		
		model.addAttribute("slips", isRepository.findByCompanyIdAndInvoiceAndSlipType(id,null,Type.INCOMMING ));
		
		return "allSlips";
	}
	
	@GetMapping("/slipsByOutType/{id}")
	public String findAllSlipsByCompanyAndOutType(Model model, @PathVariable("id") Integer id) {
		
		model.addAttribute("slips", isRepository.findByCompanyIdAndInvoiceAndSlipType(id,null,Type.OUTGOING));
		
		return "allSlips";
	}
	
	@GetMapping("/removeSlip/{id}/{sid}")
	public String removeSlipFromInvoice(@PathVariable("id") Integer id,@PathVariable("sid")  Integer sid) {
		
		docServices.removeSlipFromInvoice(id, sid);
		
		return "redirect:/invoices/"+id;
	}
	
	@GetMapping("/deleteSlip/{id}")
    public String deleteUnlinkedSlip(@PathVariable("id") Integer id) {
		
		InvoiceSlip slip = isRepository.findById(id).get();
		isRepository.delete(slip);
		
		return "redirect:/allSlips";
	}
	
	@GetMapping("/deleteInvoiceCompletelly/{id}")
	public String deleteTheInvoiceCompletelly(@PathVariable("id") Integer id) {
		docServices.deleteInvoiceIncludingTheSlips(id);
		return "redirect:/invoices";
		
	}
		
}
