package com.example.primavista.production.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.primavista.production.entity.Cut;
import com.example.primavista.production.entity.Lot;
import com.example.primavista.production.entity.Product;

@Repository
public interface LotRepository extends JpaRepository<Lot, Integer> {

	List<Lot> findAllByProduct(Product product);
	
	List<Lot> findAllByCut(Cut cut);
	
}
