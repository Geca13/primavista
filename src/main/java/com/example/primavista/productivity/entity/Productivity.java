package com.example.primavista.productivity.entity;

import java.time.LocalDate;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.springframework.format.annotation.DateTimeFormat;

import com.example.primavista.production.entity.Lot;
import com.example.primavista.production.entity.Product;
import com.example.primavista.production.entity.ProductOpers;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Table
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Productivity {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@ManyToOne
	private Employee employee;
	
	@ManyToOne
	private Product product;
	
	@ManyToOne
    private ProductOpers operation;
	
	@ManyToMany
    private List<Lot> lots;
	
	@DateTimeFormat(pattern="yyyy-MM-dd")
	private LocalDate productivityDate;
	
	private Double productivitySum;

}
