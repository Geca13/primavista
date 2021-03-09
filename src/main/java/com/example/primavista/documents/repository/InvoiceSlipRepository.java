package com.example.primavista.documents.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.primavista.documents.entity.Company;
import com.example.primavista.documents.entity.Invoice;
import com.example.primavista.documents.entity.InvoiceSlip;
import com.example.primavista.documents.entity.Type;

@Repository
public interface InvoiceSlipRepository extends JpaRepository<InvoiceSlip, Integer> {
	
	List<InvoiceSlip> findByCompanyAndInvoice(Company company, Invoice invoice);
	
	List<InvoiceSlip> findByCompanyIdAndInvoiceAndSlipType(Integer id, Invoice invoice, Type type);
	
	List<InvoiceSlip> findAllByInvoice(Invoice invoice);

}
