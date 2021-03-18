package com.example.primavista.production.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.primavista.production.entity.Product;
import com.example.primavista.production.entity.ProductOpers;

public interface ProductOperationsRepository extends JpaRepository<ProductOpers, Integer> {
	
	List<ProductOpers> findAllByProduct(Product product);
	
	List<ProductOpers> findAllByProductId(Integer id);
	
	List<ProductOpers> findAllByProductAndOperationValue(Product product, Double value);

	Boolean existsByProduct(Product product);
}
