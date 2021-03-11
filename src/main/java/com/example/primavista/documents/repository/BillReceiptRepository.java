package com.example.primavista.documents.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.example.primavista.documents.entity.BillReceipt;
import com.example.primavista.documents.entity.Company;


@Repository
public interface BillReceiptRepository extends JpaRepository<BillReceipt, Integer> {
	
	List<BillReceipt> findByCompanyAndDateBetween(Company company,LocalDate startDate , LocalDate endDate);
	
	List<BillReceipt> findByDateBetween(LocalDate startDate , LocalDate endDate);
	
	List<BillReceipt> findByCompany(Company company);

}
