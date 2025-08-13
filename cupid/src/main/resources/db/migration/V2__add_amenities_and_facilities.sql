
CREATE TABLE property_amenity (
  id BIGSERIAL PRIMARY KEY,
  hotel_id BIGINT REFERENCES property(hotel_id) ON DELETE CASCADE,
  amenity_id INTEGER,
  amenity_name TEXT
);

CREATE TABLE property_facility (
  id BIGSERIAL PRIMARY KEY,
  hotel_id BIGINT REFERENCES property(hotel_id) ON DELETE CASCADE,
  facility_id INTEGER,
  facility_name TEXT
);
