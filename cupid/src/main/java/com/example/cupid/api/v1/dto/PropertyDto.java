package com.example.cupid.api.v1.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Value;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

@Value
public class PropertyDto implements Serializable {
    @JsonProperty("hotel_id")
    Long hotelId;

    String name;

    @JsonProperty("hotel_type")
    String hotelType;

    Integer stars;
    BigDecimal rating;

    @JsonProperty("review_count")
    Integer reviewCount;

    String phone;
    String email;

    @JsonProperty("description_html")
    String descriptionHtml;

    @JsonProperty("markdown_description")
    String markdownDescription;

    @JsonProperty("important_info")
    String importantInfo;

    List<PropertyPhotoDto> photos;
    List<PropertyFacilityDto> facilities;
    List<RoomDto> rooms;
    List<PolicyDto> policies;
    List<ReviewDto> reviews;
    List<PropertyTranslationDto> translations;
}
