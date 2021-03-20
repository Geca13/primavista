package com.example.primavista.productivity.entity;

import java.time.LocalDate;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Employee {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	private String firstName;
	
	private String lastName;
	
	@DateTimeFormat(pattern="yyyy-MM-dd")
	private LocalDate bday;
	
	@DateTimeFormat(pattern="yyyy-MM-dd")
	private LocalDate employedFrom;
	
	private String emdr;
	
	private String bankAccount;
	
	private String phoneNumber;
	
	private String address;
		
}
