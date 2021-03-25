package com.example.primavista.productivity.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.example.primavista.production.entity.ProductOpers;
import com.example.primavista.productivity.entity.Employee;
import com.example.primavista.productivity.entity.Productivity;

@Repository
public interface ProductivityRepository extends JpaRepository<Productivity, Integer> {
	
	Boolean existsByEmployee(Employee employee);
	
	Productivity findByEmployee(Employee employee);

	Boolean existsByOperationAndLots_Id(ProductOpers operation, Integer lot);
	
	List<Productivity> findAllByEmployeeId(Integer id);
	
	
	
	List<Productivity> findAllByEmployeeIdAndProductivityDate(Integer id, LocalDate date);
	
	Page<Productivity> findByEmployeeIdAndProductivityDate(Integer id, LocalDate date,Pageable pageable);
	
	Page<Productivity> findAllByEmployeeIdOrderByProductivityDateDesc(Integer id,Pageable pageable);
	
}
