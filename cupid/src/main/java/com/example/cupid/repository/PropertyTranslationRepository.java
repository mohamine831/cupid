package com.example.cupid.repository;

import com.example.cupid.entity.PropertyTranslation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PropertyTranslationRepository extends JpaRepository<PropertyTranslation, Long> {
    List<PropertyTranslation> findByPropertyHotelId(Long hotelId);
}