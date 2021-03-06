package com.example.primavista.production.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.primavista.production.entity.Cut;
import com.example.primavista.production.entity.Lot;
import com.example.primavista.production.entity.Product;
import com.example.primavista.production.entity.Size;

@Repository
public interface LotRepository extends JpaRepository<Lot, Integer> {

	List<Lot> findAllByProduct(Product product);
	
	List<Lot> findAllByProductId(Integer id);
	
	List<Lot> findAllByCutId(Integer id);
	
	List<Lot> findAllByCut(Cut cut);
	
	List<Lot> findAllByCutAndSize(Cut cut, Size size);
	
	
	
}
