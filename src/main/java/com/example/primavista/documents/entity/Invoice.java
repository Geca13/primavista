package com.example.primavista.documents.entity;

import java.time.LocalDate;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Invoice {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	private Double sum;
	
	@ManyToOne
	private Company company;
	
	private String companyOut;
	
	private String invoiceNumber;
	
	@DateTimeFormat(pattern="yyyy-MM-dd")
	private LocalDate issued;
	
	@DateTimeFormat(pattern="yyyy-MM-dd")
	private LocalDate arrival;
	
	@Enumerated(EnumType.STRING)
	private Type invoiceType;
	
	@Lob
	@Column
	private String invoiceImage;
	
	@OneToMany
	private List<InvoiceSlip> slips;
}
