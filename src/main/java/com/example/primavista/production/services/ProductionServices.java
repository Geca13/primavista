package com.example.primavista.production.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.primavista.production.entity.Cut;
import com.example.primavista.production.entity.Lot;
import com.example.primavista.production.entity.Product;
import com.example.primavista.production.entity.Size;
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
	
	public Cut createNewCut(Cut cut,Integer id) {
		
		Product product = productRepository.findById(id).get();
		Cut newCut = new Cut();
		
		newCut.setProduct(product);
		newCut.setNumberOfLots(cut.getNumberOfLots());
		newCut.setNumberOfSheets(cut.getNumberOfSheets());
		cutRepository.save(newCut);
		for (int i = 1; i <= newCut.getNumberOfLots(); i++) {
			Lot lot =new Lot();
			lot.setQty(newCut.getNumberOfSheets());
			lot.setProduct(newCut.getProduct());
			lot.setCut(newCut);
			List<Lot> lots = lotRepository.findAllByProduct(newCut.getProduct());
			lot.setLid(lots.size()+1);
			lotRepository.save(lot);
		}
	return cutRepository.save(newCut);
	
	}
	
	public void addSizes(Cut cut,Lot lot) {
		List<Lot>lots = lotRepository.findAllByCut(cut);
		for (Lot lot1 : lots) {
			lot1.setSize(lot.getSize());
			lotRepository.save(lot1);
		}
	}
	
	

}
