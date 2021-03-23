package com.example.primavista.productivity.repository;

import java.time.Month;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.primavista.productivity.entity.Employee;
import com.example.primavista.productivity.entity.Salary;

@Repository
public interface SalaryRepository extends JpaRepository<Salary, Integer>{

	Boolean existsByEmployeeAndMonthAndYear(Employee employee,Month month,Integer year);
	
	Salary findByEmployeeAndMonthAndYear(Employee employee,Month month,Integer year);
	
	List<Salary>findByEmployeeOrderByMonthDesc(Employee employee);
	
	List<Salary> findByEmployeeAndMonthBetweenOrderByMonthAsc(Employee employee,Month start,Month end);

	
}
