package com.example.primavista.documents.entity;

import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
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
public class InvoiceSlip {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@ManyToOne
	private Company company;
	
	private String slipNumber;
	
	@DateTimeFormat(pattern="yyyy-MM-dd")
	private LocalDate issued;
	
	@Enumerated(EnumType.STRING)
	private Type slipType;
	
	@ManyToOne
	private Invoice invoice;
	
	@Lob
	@Column
	private String slipImage;
	
}
