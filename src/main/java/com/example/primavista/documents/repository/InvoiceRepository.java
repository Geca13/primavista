package com.example.primavista.documents.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.primavista.documents.entity.Company;
import com.example.primavista.documents.entity.Invoice;
import com.example.primavista.documents.entity.Type;

@Repository
public interface InvoiceRepository extends JpaRepository<Invoice, Integer> {

	List<Invoice> findByCompanyId(Integer id);
	
	List<Invoice> findByCompanyIdAndInvoiceType(Integer id,Type incomming);
	
}
