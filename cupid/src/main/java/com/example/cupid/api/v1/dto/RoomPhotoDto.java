package com.example.cupid.api.v1.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Value;

import java.io.Serializable;

@Value
public class RoomPhotoDto implements Serializable {
    Long id;
    String url;
}
