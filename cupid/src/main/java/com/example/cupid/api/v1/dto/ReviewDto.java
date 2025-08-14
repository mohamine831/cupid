package com.example.cupid.api.v1.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Value;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;

@Value
public class ReviewDto implements Serializable {
    Long id;
    BigDecimal rating;
    String title;
    @JsonProperty("author_name")
    String authorName;
    @JsonProperty("author_country")
    String authorCountry;
}
