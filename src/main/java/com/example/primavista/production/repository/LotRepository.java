package com.example.primavista.production.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.primavista.production.entity.Lot;

@Repository
public interface LotRepository extends JpaRepository<Lot, Integer> {

}
