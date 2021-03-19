package com.example.primavista.documents.controller;

import java.io.IOException;
import java.util.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.multipart.MultipartFile;
import com.example.primavista.documents.entity.BillReceipt;
import com.example.primavista.documents.entity.Company;
import com.example.primavista.documents.entity.Correspondence;
import com.example.primavista.documents.entity.Invoice;
import com.example.primavista.documents.entity.InvoiceSlip;
import com.example.primavista.documents.entity.Type;
import com.example.primavista.documents.repository.BillReceiptRepository;
import com.example.primavista.documents.repository.CompanyRepository;
import com.example.primavista.documents.repository.CorrespondenceRepository;
import com.example.primavista.documents.repository.InstitutionRepository;
import com.example.primavista.documents.repository.InvoiceRepository;
import com.example.primavista.documents.repository.InvoiceSlipRepository;
import com.example.primavista.documents.services.DocumentsServices;
import com.example.primavista.documents.services.YouMustEnterCompanyException;

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
	
	@Autowired
    BillReceiptRepository receiptRepository;
	
	@Autowired
	InstitutionRepository institutionRepository;
	
	@Autowired
	CorrespondenceRepository corRepository;
	
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
	
	@GetMapping("/updateCompany/{id}")
	public String getUpdateForm(@PathVariable("id")Integer id, Model model) {
		
		model.addAttribute("company", companyRepository.findById(id).get());
		
		return "updateCompany";
	}
	
	@PostMapping("/updateCompany/{id}")
	public String completeUpdate(@PathVariable("id")Integer id,Company company) {
		
		docServices.updateCompany(id, company);
		
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
		model.addAttribute("companies", companyRepository.findAll());
		
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
		
		if(company.getCompanyName().equalsIgnoreCase("PrimaVista")) {
			newInvoice.setInvoiceType(Type.OUTGOING);
			newInvoice.setIssued(invoice.getIssued());
			newInvoice.setArrival(invoice.getArrival());
			newInvoice.setCompany(company);
			newInvoice.setCompanyOut(invoice.getCompanyOut());
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
		
		newInvoice.setInvoiceType(Type.INCOMMING);
		newInvoice.setIssued(invoice.getIssued());
		newInvoice.setArrival(invoice.getArrival());
		newInvoice.setCompany(company);
		newInvoice.setCompanyOut(companyRepository.findByCompanyName("PrimaVista"));
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
		model.addAttribute("companies", companyRepository.findAll());
		return "allInvoices";
	}
	
	@GetMapping("/invoices/{id}")
	public String findAllInvoicesByCompany(Model model, @PathVariable("id") Integer id) {
		
		model.addAttribute("invoices", invoiceRepository.findByCompanyId(id));
		
		return "allInvoices";
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
	
	@GetMapping("/deleteInvoiceKeepSlips/{id}")
	public String deleteTheInvoicewithoutSlips(@PathVariable("id") Integer id) {
		docServices.deleteInvoiceKeepTheSlips(id);
		return "redirect:/invoices";
	}
	
	@GetMapping("/changeInvoicePhoto/{id}")
	public String getPhotoForm(Model model,@PathVariable("id") Integer id, MultipartFile file) {
		
		model.addAttribute("invoice", invoiceRepository.findById(id).get());
		model.addAttribute("file", file);
		return "photoForm";
	}
	
	@PostMapping("/changeInvoicePhoto/{id}")
	public String updateInvoiceImage(@PathVariable("id") Integer id, MultipartFile file) {
		docServices.changeInvoicePhoto(id, file);
		return "redirect:/invoices";
	}
	
	@GetMapping("/changeSlipPhoto/{id}")
	public String getSlipPhotoForm(Model model,@PathVariable("id") Integer id, MultipartFile file) {
		
		model.addAttribute("slip", isRepository.findById(id).get());
		model.addAttribute("file", file);
		return "slipPhotoForm";
	}
	
	@PostMapping("/changeSlipPhoto/{id}")
	public String updateSlipImage(@PathVariable("id") Integer id, MultipartFile file) {
		docServices.changeSlipPhoto(id, file);
		return "redirect:/allSlips";
	}
	
	@GetMapping("/updateSlip/{id}")
	public String updateSlipWithoutImageForm(Model model , @PathVariable("id")Integer id) {
		model.addAttribute("slip", isRepository.findById(id).get());
		model.addAttribute("companies", companyRepository.findAll());
		return "updateSlip";
	}
	
	@PostMapping("/updateSlip/{id}")
	public String completeSlipUpdate(@ModelAttribute("slip") InvoiceSlip slip, @PathVariable("id") Integer id) {
		docServices.updateInvoiceSlipwithoutImage(id, slip);
		return "redirect:/allSlips";
	}
	
	@GetMapping("/updateInvoice/{id}")
	public String updateInvoiceWithoutImageForm(Model model , @PathVariable("id")Integer id) {
		model.addAttribute("invoice", invoiceRepository.findById(id).get());
		model.addAttribute("companies", companyRepository.findAll());
		model.addAttribute("slips", isRepository.findAllByInvoice(invoiceRepository.findById(id).get()));
		return "updateInvoiceForm";
	}
	
	@PostMapping("/updateInvoice/{id}")
	public String completeInvoiceUpdate(@ModelAttribute("invoice") Invoice invoice, @PathVariable("id") Integer id) {
		docServices.updateInvoiceWithoutImage(id, invoice);
		return "redirect:/invoices";
	}
	
	@GetMapping("/newReceipt")
	public String addNewReceipt(Model model, MultipartFile file) {
		
		model.addAttribute("file", file);
		model.addAttribute("receipt", new BillReceipt());
		model.addAttribute("companies", companyRepository.findAll());
		return "newReceiptForm";
	}
	
	@PostMapping("/newReceipt")
	public String createNewReceipt(@ModelAttribute("receipt") BillReceipt receipt, MultipartFile file,@Param(value="oldCompany")String oldCompany,@Param(value="newCompany")String newCompany) {
		
		if(oldCompany.equals("") && newCompany.equals("")) {
			return "redirect:/newReceipt?noCompanySelected";
		}
		
		if(companyRepository.existsByCompanyNameIgnoreCase(newCompany)) {
			return "redirect:/newReceipt?companyExists";
		}
		
		try {
			docServices.saveBillReceipt(receipt, file,oldCompany, newCompany);
		} catch (YouMustEnterCompanyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "redirect:/";
	}
	
	@GetMapping("/deleteReceipt/{id}")
	public String deleteReceipt(@PathVariable("id")Integer id) {
		
		docServices.deleteBillReceipt(id);
		return "redirect:/allReceipts";
	}
	
	@GetMapping("/allReceipts")
	public String getAllReceipts(Model model) {
		
		model.addAttribute("companies", companyRepository.findAll());
		model.addAttribute("receipts", receiptRepository.findAll());
		return "allReceiptsPage";
	}
	
	@GetMapping("/newCorrespondence")
	public String addNewCorrespondence(Model model, MultipartFile file) {
		
		model.addAttribute("file", file);
		model.addAttribute("correspondence", new Correspondence());
		model.addAttribute("institutions", institutionRepository.findAll());
		
		return "newCorrespondence";
	}
	
	@PostMapping("/newCorrespondence")
	public String createNewCorrespondence(@ModelAttribute("correspondence") Correspondence correspondence, MultipartFile file,@Param(value="oldInstitution")String oldInstitution,@Param(value="newInstitution")String newInstitution) {
		
		if(oldInstitution.equals("") && newInstitution.equals("")) {
			return "redirect:/newCorrespondence?noCompanySelected";
		}
		if(institutionRepository.existsByInstitutionNameIgnoreCase(newInstitution)) {
			return "redirect:/newCorrespondence?institutionallreadiexists";
		}
		
		docServices.saveNewCorrespondence(correspondence, file, oldInstitution, newInstitution);
		
		return "redirect:/";
	}
	
	@GetMapping("/allCorrespondence")
	public String allCorrespondences(Model model) {
		
		model.addAttribute("allCorrespondence", corRepository.findAll());
		model.addAttribute("institutions", institutionRepository.findAll());
		return "allCorrespondence";
		
	}
	
	@GetMapping("/deleteCorrespondence/{id}")
	public String deleteCorrespondence(@PathVariable("id")Integer id) {
		
		docServices.deleteCorrespondence(id);
		return "redirect:/allCorrespondence";
		
	}
	
	
}
