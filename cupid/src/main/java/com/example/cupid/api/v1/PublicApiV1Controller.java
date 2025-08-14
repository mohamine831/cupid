package com.example.cupid.api.v1;

import com.example.cupid.api.v1.dto.PropertyDto;
import com.example.cupid.api.v1.dto.ReviewDto;
import com.example.cupid.api.v1.dto.PropertyTranslationDto;
import com.example.cupid.repository.PropertyRepository;
import com.example.cupid.repository.ReviewRepository;
import com.example.cupid.repository.PropertyTranslationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class PublicApiV1Controller {

    private final PropertyRepository propertyRepository;
    private final ReviewRepository reviewRepository;
    private final PropertyTranslationRepository propertyTranslationRepository;
    private final V1Mapper mapper;

    @GetMapping("/hotels")
    public Page<PropertyDto> getHotels(Pageable pageable) {
        return propertyRepository.findAll(pageable)
                .map(mapper::toDto);
    }

    @GetMapping("/hotels/{hotelId}")
    public ResponseEntity<PropertyDto> getHotelById(@PathVariable Long hotelId) {
        return propertyRepository.findById(hotelId)
                .map(mapper::toDto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/hotels/{hotelId}/reviews")
    public ResponseEntity<List<ReviewDto>> getReviewsByHotelId(@PathVariable Long hotelId) {
        if (!propertyRepository.existsById(hotelId)) {
            return ResponseEntity.notFound().build();
        }
        List<ReviewDto> reviews = reviewRepository.findByPropertyHotelId(hotelId).stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(reviews);
    }

    @GetMapping("/hotels/{hotelId}/translations")
    public ResponseEntity<List<PropertyTranslationDto>> getTranslationsByHotelId(@PathVariable Long hotelId) {
        if (!propertyRepository.existsById(hotelId)) {
            return ResponseEntity.notFound().build();
        }
        List<PropertyTranslationDto> translations = propertyTranslationRepository.findByPropertyHotelId(hotelId).stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(translations);
    }
}
