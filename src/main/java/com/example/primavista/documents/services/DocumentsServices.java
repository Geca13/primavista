package com.example.primavista.documents.services;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashSet;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.example.primavista.documents.entity.Company;
import com.example.primavista.documents.entity.Invoice;
import com.example.primavista.documents.entity.InvoiceSlip;
import com.example.primavista.documents.repository.CompanyRepository;
import com.example.primavista.documents.repository.InvoiceRepository;
import com.example.primavista.documents.repository.InvoiceSlipRepository;

@Service
public class DocumentsServices {
	
	@Autowired
	CompanyRepository companyRepository;
	
	@Autowired
	InvoiceSlipRepository slipRepository;
	
	@Autowired
	InvoiceRepository invoiceRepository;
	
	public Company saveNewCompany(Company company) {
		
		Company newCompany = new Company();
		newCompany.setAccountNumber(company.getAccountNumber());
		newCompany.setAddress(company.getAddress());
		newCompany.setCompanyName(company.getCompanyName());
		
		return companyRepository.save(newCompany);
		
		
	}
	
	public InvoiceSlip saveNewInvoiceSlip(InvoiceSlip slip,Integer id, MultipartFile file) {
		
		InvoiceSlip newSlip = new InvoiceSlip();
		Company company = companyRepository.findById(id).get();
		
		newSlip.setCompany(company);
		newSlip.setIssued(LocalDate.now());
		newSlip.setPartyNumber(slip.getPartyNumber());
		newSlip.setSum(slip.getSum());
		newSlip.setSlipType(slip.getSlipType());
		String fileName = StringUtils.cleanPath(file.getOriginalFilename());
	       if(fileName.contains("..")) {
	       	System.out.println("not a valid file");
	        }
		  try {
			  newSlip.setSlipImage(Base64.getEncoder().encodeToString(file.getBytes()));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		return slipRepository.save(newSlip);
				
	}
	
	public Invoice createNewInvoice(Invoice invoice, MultipartFile file, Integer id, Integer sid) {
		Company company = companyRepository.findById(id).get();
		Invoice newInvoice = new Invoice();
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
		  List<InvoiceSlip> slips = new ArrayList<>();
		  List<InvoiceSlip>currentSlipsNotInInvoice = slipRepository.findByCompanyAndInvoice(company, null);
		  for (InvoiceSlip invoiceSlip : currentSlipsNotInInvoice) {
			
			if(invoiceSlip.getId() == sid) {
				InvoiceSlip slip = slipRepository.findById(sid).get();
			
			slips.add(slip);
			invoiceRepository.save(newInvoice);
			slip.setInvoice(newInvoice);
			slipRepository.save(slip);
			}
		}
		  newInvoice.setSlips(slips);
		
		return invoiceRepository.save(newInvoice);
		
	}

}
