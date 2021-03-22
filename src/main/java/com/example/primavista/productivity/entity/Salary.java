package com.example.primavista.productivity.entity;

import java.time.Month;
import java.time.Year;
import java.time.YearMonth;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Salary {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@ManyToOne
	private Employee employee;
	
	@ManyToMany
	private List<Productivity> productivities;
	
	@Enumerated(EnumType.STRING)
	private Month month;
	
	private Integer year;
	
	private Double salary;
}
