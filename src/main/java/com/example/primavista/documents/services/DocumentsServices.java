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
		newCompany.setEmail(company.getEmail());
		
		return companyRepository.save(newCompany);
		
		
	}
	
	public InvoiceSlip saveNewInvoiceSlip(InvoiceSlip slip,Integer id, MultipartFile file) {
		
		InvoiceSlip newSlip = new InvoiceSlip();
		Company company = companyRepository.findById(id).get();
		
		newSlip.setCompany(company);
		newSlip.setIssued(slip.getIssued());
		newSlip.setSlipNumber(slip.getSlipNumber());
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
	
	
	public Invoice removeSlipFromInvoice(Integer id,Integer sid) {
		Invoice invoice = invoiceRepository.findById(id).get();
		InvoiceSlip slip = slipRepository.findById(sid).get();
		invoice.getSlips().remove(slip);
		slip.setInvoice(null);
		slipRepository.save(slip);
		return invoiceRepository.save(invoice);
		
	}
	
	public void deleteInvoiceIncludingTheSlips(Integer id) {
		
		Invoice invoice = invoiceRepository.findById(id).get();
		List<InvoiceSlip> slips = slipRepository.findAllByInvoice(invoice);
		for (InvoiceSlip invoiceSlip : slips) {
			invoice.getSlips().remove(invoiceSlip);
			invoiceRepository.save(invoice);
			invoiceSlip.setInvoice(null);
			slipRepository.save(invoiceSlip);
		}
		slipRepository.deleteAll(slips);
		
		invoiceRepository.delete(invoice);
	}
	

}
