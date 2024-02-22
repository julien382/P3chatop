package com.openclassrooms.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.openclassrooms.entity.Rental;

@Repository
public interface RentalRepository extends JpaRepository<Rental, Long>{
    
}