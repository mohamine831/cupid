package com.example.cupid.api.v1.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Value;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

@Value
public class RoomDto implements Serializable {
    Long id;
    String name;
    @JsonProperty("pax")
    Integer pax;
    BigDecimal size;
    @JsonProperty("size_unit")
    String sizeUnit;
    List<RoomPhotoDto> photos;
    List<RoomAmenityDto> amenities;
}
