package com.example.cupid.api.v1.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Value;

import java.io.Serializable;

@Value
public class PropertyTranslationDto implements Serializable {
    Long id;
    String language;
    @JsonProperty("description_html")
    String descriptionHtml;
    @JsonProperty("markdown_description")
    String markdownDescription;
}
