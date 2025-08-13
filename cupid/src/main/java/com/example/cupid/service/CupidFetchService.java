package com.example.cupid.service;

import com.example.cupid.client.CupidApiClient;
import com.example.cupid.entity.*;
import com.example.cupid.repository.*;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class CupidFetchService {
    private final CupidApiClient apiClient;
    private final PropertyRepository propertyRepository;
    private final PropertyTranslationRepository translationRepository;
    private final ReviewRepository reviewRepository;
    private final PropertyPhotoRepository propertyPhotoRepository;
    private final RoomPhotoRepository roomPhotoRepository;
    private final PropertyAmenityRepository propertyAmenityRepository;
    private final PropertyFacilityRepository propertyFacilityRepository;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Transactional
    public void fetchAndSave(long hotelId, int reviewsToFetch) {
        log.info("Starting to fetch and save hotel with ID: {}", hotelId);

        try {
            Mono<JsonNode> propMono = apiClient.fetchProperty(hotelId);
            log.info("Created Mono for hotel ID: {}", hotelId);

            JsonNode propResp = propMono.block();
            log.info("API response for hotel ID {}: {}", hotelId, propResp);

            if (propResp == null) {
                log.error("API response is null for hotel ID: {}", hotelId);
                return;
            }


            JsonNode data = propResp;
            log.info("Property data for hotel ID {}: {}", hotelId, data);

            Property p = mapProperty(data);
            Property savedProperty = propertyRepository.save(p);
            log.info("Saved property: {} with hotel ID: {}", savedProperty.getName(), savedProperty.getHotelId());

            // translations
            for (String lang : new String[]{"fr", "es"}) {
                try {
                    JsonNode tnode = apiClient.fetchTranslation(hotelId, lang).block();
                    if (tnode != null && tnode != null && !tnode.isNull()) {
                        PropertyTranslation tr = new PropertyTranslation();
                        tr.setHotelId(hotelId);
                        tr.setLang(lang);
                        JsonNode td = tnode;
                        tr.setDescriptionHtml(td.has("description") ? td.get("description").asText() : null);
                        tr.setMarkdownDescription(td.has("markdown_description") ? td.get("markdown_description").asText() : null);
                        translationRepository.findByHotelIdAndLang(hotelId, lang)
                                .ifPresentOrElse(existing -> {
                                    existing.setDescriptionHtml(tr.getDescriptionHtml());
                                    existing.setMarkdownDescription(tr.getMarkdownDescription());
                                    existing.setFetchedAt(Instant.now());
                                    PropertyTranslation savedTranslation = translationRepository.save(existing);
                                    log.info("Updated translation for hotel ID: {} and language: {}", savedTranslation.getHotelId(), savedTranslation.getLang());
                                }, () -> {
                                    PropertyTranslation savedTranslation = translationRepository.save(tr);
                                    log.info("Saved new translation for hotel ID: {} and language: {}", savedTranslation.getHotelId(), savedTranslation.getLang());
                                });
                    } else {
                        log.debug("No translation data found for hotel ID: {} and language: {}", hotelId, lang);
                    }
                } catch (Exception e) {
                    log.error("Error fetching translation for hotel ID: {} and language: {}", hotelId, lang, e);
                }
            }

            // reviews
            try {
                JsonNode rnode = apiClient.fetchReviews(hotelId, reviewsToFetch).block();
                if (rnode != null && rnode != null && rnode.isArray()) {
                    Iterator<JsonNode> it = rnode.elements();
                    int reviewCount = 0;
                    while (it.hasNext()) {
                        JsonNode rn = it.next();
                        Review review = new Review();
                        review.setHotelId(hotelId);
                        if (rn.has("average_score")) review.setAverageScore(rn.get("average_score").decimalValue());
                        review.setCountry(rn.has("country") ? rn.get("country").asText() : null);
                        review.setType(rn.has("type") ? rn.get("type").asText() : null);
                        review.setName(rn.has("name") ? rn.get("name").asText() : null);
                        if (rn.has("date")) {
                            String ds = rn.get("date").asText();
                            // attempt parse: yyyy-MM-dd HH:mm:ss
                            try {
                                LocalDateTime ldt = LocalDateTime.parse(ds.replace(" ", "T"));
                                review.setReviewDate(ldt.toInstant(ZoneOffset.UTC));
                            } catch (Exception e) {
                                review.setReviewDate(Instant.now());
                            }
                        }
                        review.setHeadline(rn.has("headline") ? rn.get("headline").asText() : null);
                        review.setLanguage(rn.has("language") ? rn.get("language").asText() : null);
                        review.setPros(rn.has("pros") ? rn.get("pros").asText() : null);
                        review.setCons(rn.has("cons") ? rn.get("cons").asText() : null);
                        review.setSource(rn.has("source") ? rn.get("source").asText() : null);
                        try {
                            review.setRawJson(objectMapper.writeValueAsString(rn));
                        } catch (Exception ignored) {}
                        Review savedReview = reviewRepository.save(review);
                        reviewCount++;
                        log.debug("Saved review ID: {} for hotel ID: {}", savedReview.getId(), savedReview.getHotelId());
                    }
                    log.info("Saved {} reviews for hotel ID: {}", reviewCount, hotelId);
                } else {
                    log.debug("No review data found for hotel ID: {}", hotelId);
                }
            } catch (Exception e) {
                log.error("Error fetching reviews for hotel ID: {}", hotelId, e);
            }

            log.info("Successfully completed fetch and save for hotel ID: {}", hotelId);

        } catch (Exception e) {
            log.error("Error in fetchAndSave for hotel ID: {}", hotelId, e);
            throw e; // Re-throw to trigger transaction rollback
        }
    }

    private Property mapProperty(JsonNode data) {
        Property p = propertyRepository.findById(data.get("hotel_id").asLong()).orElse(new Property());

        if (p.getPhotos() != null) {
            p.getPhotos().clear();
        } else {
            p.setPhotos(new ArrayList<>());
        }
        if (data.has("photos") && data.get("photos").isArray()) {
            for (JsonNode photoNode : data.get("photos")) {
                PropertyPhoto photo = new PropertyPhoto();
                photo.setProperty(p);
                photo.setUrl(photoNode.has("url") ? photoNode.get("url").asText() : null);
                photo.setHdUrl(photoNode.has("hd_url") ? photoNode.get("hd_url").asText() : null);
                p.getPhotos().add(photo);
            }
        }

        if (p.getAmenities() != null) {
            p.getAmenities().clear();
        } else {
            p.setAmenities(new ArrayList<>());
        }
        if (data.has("amenities") && data.get("amenities").isArray()) {
            for (JsonNode amenityNode : data.get("amenities")) {
                PropertyAmenity amenity = new PropertyAmenity();
                amenity.setProperty(p);
                amenity.setAmenityId(amenityNode.has("id") ? amenityNode.get("id").asInt() : null);
                amenity.setAmenityName(amenityNode.has("name") ? amenityNode.get("name").asText() : null);
                p.getAmenities().add(amenity);
            }
        }

        if (p.getFacilities() != null) {
            p.getFacilities().clear();
        } else {
            p.setFacilities(new ArrayList<>());
        }
        if (data.has("facilities") && data.get("facilities").isArray()) {
            for (JsonNode facilityNode : data.get("facilities")) {
                PropertyFacility facility = new PropertyFacility();
                facility.setProperty(p);
                facility.setFacilityId(facilityNode.has("id") ? facilityNode.get("id").asInt() : null);
                facility.setFacilityName(facilityNode.has("name") ? facilityNode.get("name").asText() : null);
                p.getFacilities().add(facility);
            }
        }


        p.setHotelId(data.has("hotel_id") ? data.get("hotel_id").asLong() : null);
        p.setCupidId(data.has("cupid_id") ? data.get("cupid_id").asLong() : null);
        p.setName(data.has("hotel_name") ? data.get("hotel_name").asText() : null);
        p.setHotelType(data.has("hotel_type") ? data.get("hotel_type").asText() : null);
        p.setHotelTypeId(data.has("hotel_type_id") ? data.get("hotel_type_id").asInt() : null);
        p.setChain(data.has("chain") ? data.get("chain").asText() : null);
        p.setChainId(data.has("chain_id") ? data.get("chain_id").asInt() : null);
        p.setLatitude(data.has("latitude") ? data.get("latitude").asDouble() : null);
        p.setLongitude(data.has("longitude") ? data.get("longitude").asDouble() : null);
        p.setPhone(data.has("phone") ? data.get("phone").asText() : null);
        p.setEmail(data.has("email") ? data.get("email").asText() : null);
        p.setStars(data.has("stars") ? data.get("stars").asInt() : null);
        p.setRating(data.has("rating") ? data.get("rating").decimalValue() : null);
        p.setReviewCount(data.has("review_count") ? data.get("review_count").asInt() : null);
        p.setDescriptionHtml(data.has("description") ? data.get("description").asText() : null);
        p.setMarkdownDescription(data.has("markdown_description") ? data.get("markdown_description").asText() : null);
        p.setImportantInfo(data.has("important_info") ? data.get("important_info").asText() : null);
        try {
            p.setRawJson(objectMapper.writeValueAsString(data));
        } catch (Exception ignored) {}
        p.setUpdatedAt(Instant.now());
        return p;
    }
}
