package com.example.primavista.documents.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.example.primavista.documents.entity.Correspondence;
import com.example.primavista.documents.entity.Institution;

@Repository
public interface CorrespondenceRepository extends JpaRepository<Correspondence, Integer> {
	
    List<Correspondence> findByInstitutionAndDateBetween(Institution institution,LocalDate startDate , LocalDate endDate);
	
	List<Correspondence> findByDateBetween(LocalDate startDate , LocalDate endDate);
	
	List<Correspondence> findByInstitution(Institution institution);
	
	List<Correspondence> findAllByOrderByDateAsc();



}
