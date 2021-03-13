package com.example.primavista.production.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.primavista.production.entity.Cut;
import com.example.primavista.production.entity.Lot;
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
	
	public Cut createNewCut(Cut cut) {
		cutRepository.save(cut);
		for (int i = 1; i <= cut.getNumberOfLots(); i++) {
			Lot lot =new Lot();
			lot.setQty(cut.getNumberOfSheets());
			lot.setProduct(cut.getProduct());
			lot.setCut(cut);
			List<Lot> lots = lotRepository.findAllByProduct(cut.getProduct());
			lot.setLid(lots.size()+1);
			lotRepository.save(lot);
		}
		
	return cutRepository.save(cut);
	}
	
	

}
