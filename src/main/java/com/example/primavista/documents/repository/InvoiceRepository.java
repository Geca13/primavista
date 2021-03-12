package com.example.primavista.documents.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.primavista.documents.entity.BillReceipt;
import com.example.primavista.documents.entity.Company;
import com.example.primavista.documents.entity.Invoice;
import com.example.primavista.documents.entity.Type;

@Repository
public interface InvoiceRepository extends JpaRepository<Invoice, Integer> {

	List<Invoice> findByCompanyId(Integer id);
	
	List<Invoice> findByCompanyIdAndInvoiceType(Integer id,Type incomming);
	
	List<Invoice> findByCompany(Company company);
	
	List<Invoice> findByCompanyAndIssuedBetween(Company company,LocalDate startDate , LocalDate endDate);
	
	List<Invoice> findByIssuedBetween(LocalDate startDate , LocalDate endDate);
	
	List<Invoice> findAllByOrderByIssuedAsc();
	
	
	
}
