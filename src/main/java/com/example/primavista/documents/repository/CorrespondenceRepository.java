package com.example.primavista.documents.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.primavista.documents.entity.Correspondence;

@Repository
public interface CorrespondenceRepository extends JpaRepository<Correspondence, Integer> {

}
