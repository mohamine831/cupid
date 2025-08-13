package com.example.cupid.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.Instant;

@Entity
@Table(name = "review")
@Data
public class Review {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "hotel_id")
    private Long hotelId;
    
    @Column(name = "average_score")
    private BigDecimal averageScore;
    
    @Column(name = "country")
    private String country;
    
    @Column(name = "type")
    private String type;
    
    @Column(name = "name")
    private String name;

    @Column(name = "review_date")
    private Instant reviewDate;

    @Column(name = "headline")
    private String headline;
    
    @Column(name = "language")
    private String language;

    @Column(columnDefinition = "text")
    private String pros;

    @Column(columnDefinition = "text")
    private String cons;

    @Column(name = "source")
    private String source;

    @Column(columnDefinition = "jsonb")
    private String rawJson;
}