CREATE TABLE policy (
    id BIGSERIAL PRIMARY KEY,
    hotel_id BIGINT REFERENCES property(hotel_id) ON DELETE CASCADE,
    policy_type VARCHAR(255),
    name VARCHAR(255),
    description TEXT
);

CREATE TABLE room_amenity (
    id BIGSERIAL PRIMARY KEY,
    room_id BIGINT REFERENCES room(id) ON DELETE CASCADE,
    amenities_id INTEGER,
    name VARCHAR(255),
    sort INTEGER
);

DROP TABLE IF EXISTS property_amenity;
