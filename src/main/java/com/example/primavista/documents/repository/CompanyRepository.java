package com.example.primavista.documents.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.primavista.documents.entity.Company;

@Repository
public interface CompanyRepository extends JpaRepository<Company, Integer> {

	Company findByCompanyName(String companyName);
	
	Boolean existsByCompanyNameIgnoreCase(String companyName);
	
}
