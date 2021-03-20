package com.example.primavista.productivity.repository;



import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import com.example.primavista.productivity.entity.Employee;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Integer> {
	
	@Query("SELECT e FROM Employee e WHERE CONCAT(e.firstName, ' ', e.lastName, ' ', e.emdr, ' ', e.bankAccount, ' ', e.phoneNumber, ' ', e.id) LIKE %?1%")
	Page<Employee> findBySearch(String search,Pageable pageable);

}
