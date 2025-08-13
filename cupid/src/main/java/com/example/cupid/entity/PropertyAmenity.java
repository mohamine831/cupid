package com.example.cupid.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "property_amenity")
@Data
public class PropertyAmenity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "hotel_id")
    private Property property;

    @Column(name = "amenity_id")
    private Integer amenityId;

    @Column(name = "amenity_name")
    private String amenityName;
}
