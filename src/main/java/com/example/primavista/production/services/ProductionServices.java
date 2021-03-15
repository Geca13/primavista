package com.example.primavista.production.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.primavista.production.entity.Product;
import com.example.primavista.production.repository.CutRepository;
import com.example.primavista.production.repository.LotRepository;
import com.example.primavista.production.repository.OperationRepository;
import com.example.primavista.production.repository.ProductRepository;

@Service
public class ProductionServices {
	
	@Autowired
	ProductRepository productRepository;
	
	@Autowired
	OperationRepository operationRepository;
	
	@Autowired
	CutRepository cutRepository;
	
	@Autowired
	LotRepository lotRepository;
	
	public Product saveNewProduct(Product product) {
		product.setCompleted(false);
		return productRepository.save(product);
	}
	
	}
	
	


