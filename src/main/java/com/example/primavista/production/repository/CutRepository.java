package com.example.primavista.production.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.primavista.production.entity.Cut;

@Repository
public interface CutRepository extends JpaRepository<Cut, Integer> {

	
}
