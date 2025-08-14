package com.example.cupid.api.v1;

import com.example.cupid.api.v1.dto.*;
import com.example.cupid.entity.*;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class V1Mapper {

    public PropertyDto toDto(Property property) {
        if (property == null) {
            return null;
        }
        return new PropertyDto(
                property.getHotelId(),
                property.getName(),
                property.getHotelType(),
                property.getStars(),
                property.getRating(),
                property.getReviewCount(),
                property.getPhone(),
                property.getEmail(),
                property.getDescriptionHtml(),
                property.getMarkdownDescription(),
                property.getImportantInfo(),
                property.getPhotos() != null ? property.getPhotos().stream().map(this::toDto).collect(Collectors.toList()) : Collections.emptyList(),
                property.getFacilities() != null ? property.getFacilities().stream().map(this::toDto).collect(Collectors.toList()) : Collections.emptyList(),
                property.getRooms() != null ? property.getRooms().stream().map(this::toDto).collect(Collectors.toList()) : Collections.emptyList(),
                property.getPolicies() != null ? property.getPolicies().stream().map(this::toDto).collect(Collectors.toList()) : Collections.emptyList(),
                property.getReviews() != null ? property.getReviews().stream().map(this::toDto).collect(Collectors.toList()) : Collections.emptyList(),
                property.getTranslations() != null ? property.getTranslations().stream().map(this::toDto).collect(Collectors.toList()) : Collections.emptyList()
        );
    }

    public PropertyPhotoDto toDto(PropertyPhoto photo) {
        if (photo == null) {
            return null;
        }
        return new PropertyPhotoDto(photo.getId(), photo.getUrl());
    }

    public PropertyFacilityDto toDto(PropertyFacility facility) {
        if (facility == null) {
            return null;
        }
        return new PropertyFacilityDto(facility.getId(), facility.getFacilityName());
    }

    public RoomDto toDto(Room room) {
        if (room == null) {
            return null;
        }
        return new RoomDto(
                room.getId(),
                room.getRoomName(),
                room.getMaxOccupancy(),
                room.getRoomSizeSquare(),
                room.getRoomSizeUnit(),
                room.getPhotos() != null ? room.getPhotos().stream().map(this::toDto).collect(Collectors.toList()) : Collections.emptyList(),
                room.getAmenities() != null ? room.getAmenities().stream().map(this::toDto).collect(Collectors.toList()) : Collections.emptyList()
        );
    }

    public RoomPhotoDto toDto(RoomPhoto photo) {
        if (photo == null) {
            return null;
        }
        return new RoomPhotoDto(photo.getId(), photo.getUrl());
    }

    public RoomAmenityDto toDto(RoomAmenity amenity) {
        if (amenity == null) {
            return null;
        }
        return new RoomAmenityDto(amenity.getId(), amenity.getName());
    }

    public PolicyDto toDto(Policy policy) {
        if (policy == null) {
            return null;
        }
        return new PolicyDto(policy.getId(), policy.getName(), policy.getDescription());
    }

    public ReviewDto toDto(Review review) {
        if (review == null) {
            return null;
        }
        return new ReviewDto(
                review.getId(),
                review.getAverageScore(),
                review.getHeadline(),
                review.getName(),
                review.getCountry()
        );
    }

    public PropertyTranslationDto toDto(PropertyTranslation translation) {
        if (translation == null) {
            return null;
        }
        return new PropertyTranslationDto(
                translation.getId(),
                translation.getLang(),
                translation.getDescriptionHtml(),
                translation.getMarkdownDescription()
        );
    }
}
