package com.example.cupid.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.Instant;

@Entity
@Table(name = "property_translation", uniqueConstraints = @UniqueConstraint(columnNames = {"hotel_id", "lang"}))
@Data
public class PropertyTranslation {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "hotel_id")
    private Long hotelId;
    
    @Column(name = "lang")
    private String lang;

    @Column(columnDefinition = "text")
    private String descriptionHtml;

    @Column(columnDefinition = "text")
    private String markdownDescription;

    @Column(name = "fetched_at")
    private Instant fetchedAt = Instant.now();
}