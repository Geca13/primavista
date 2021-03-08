package com.example.primavista.documents.entity;

import java.time.LocalDate;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

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
	
	private String partyNumber;
	
	private LocalDate issued;
	
	private LocalDate arrival;
	
	@Enumerated
	private Type invoiceType;
	
	@Lob
	@Column
	private String invoiceImage;
	
	@OneToMany
	private List<InvoiceSlip> slips;
}
