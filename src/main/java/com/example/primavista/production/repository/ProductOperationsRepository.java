package com.example.primavista.production.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.primavista.production.entity.Product;
import com.example.primavista.production.entity.ProductOpers;

public interface ProductOperationsRepository extends JpaRepository<ProductOpers, Integer> {
	
	List<ProductOpers> findByProduct(Product product);

}
