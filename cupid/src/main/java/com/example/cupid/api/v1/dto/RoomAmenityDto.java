package com.example.cupid.api.v1.dto;

import lombok.Value;

import java.io.Serializable;

@Value
public class RoomAmenityDto implements Serializable {
    Long id;
    String name;
}
