package com.example.primavista.documents.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.primavista.documents.entity.Institution;

@Repository
public interface InstitutionRepository extends JpaRepository<Institution, Integer> {
	
	Institution findByInstitutionName(String institutionName);

}
