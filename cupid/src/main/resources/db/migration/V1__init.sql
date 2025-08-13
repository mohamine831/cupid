-- Flyway migration: create core tables
CREATE TABLE property (
  hotel_id BIGINT PRIMARY KEY,
  cupid_id BIGINT,
  name TEXT,
  hotel_type TEXT,
  hotel_type_id INTEGER,
  chain TEXT,
  chain_id INTEGER,
  latitude DOUBLE PRECISION,
  longitude DOUBLE PRECISION,
  phone TEXT,
  email TEXT,
  address_json JSONB,
  stars INTEGER,
  rating NUMERIC,
  review_count INTEGER,
  description_html TEXT,
  markdown_description TEXT,
  important_info TEXT,
  raw_json JSONB,
  created_at TIMESTAMP WITH TIME ZONE DEFAULT now(),
  updated_at TIMESTAMP WITH TIME ZONE DEFAULT now()
);

CREATE TABLE property_translation (
  id BIGSERIAL PRIMARY KEY,
  hotel_id BIGINT REFERENCES property(hotel_id) ON DELETE CASCADE,
  lang VARCHAR(4) NOT NULL,
  description_html TEXT,
  markdown_description TEXT,
  fetched_at TIMESTAMP WITH TIME ZONE DEFAULT now(),
  UNIQUE(hotel_id, lang)
);

CREATE TABLE property_photo (
  id BIGSERIAL PRIMARY KEY,
  hotel_id BIGINT REFERENCES property(hotel_id) ON DELETE CASCADE,
  url TEXT,
  hd_url TEXT,
  image_description TEXT,
  image_class1 TEXT,
  main_photo BOOLEAN,
  score NUMERIC,
  class_id INTEGER,
  class_order INTEGER
);

CREATE TABLE room (
  id BIGINT PRIMARY KEY,
  hotel_id BIGINT REFERENCES property(hotel_id) ON DELETE CASCADE,
  room_name TEXT,
  description TEXT,
  room_size_square NUMERIC,
  room_size_unit TEXT,
  max_adults INTEGER,
  max_children INTEGER,
  max_occupancy INTEGER,
  raw_json JSONB
);

CREATE TABLE room_photo (
  id BIGSERIAL PRIMARY KEY,
  room_id BIGINT REFERENCES room(id) ON DELETE CASCADE,
  url TEXT,
  hd_url TEXT,
  image_description TEXT,
  main_photo BOOLEAN,
  score NUMERIC
);

CREATE TABLE review (
  id BIGSERIAL PRIMARY KEY,
  hotel_id BIGINT REFERENCES property(hotel_id) ON DELETE CASCADE,
  average_score NUMERIC,
  country VARCHAR(10),
  type TEXT,
  name TEXT,
  review_date TIMESTAMP,
  headline TEXT,
  language VARCHAR(8),
  pros TEXT,
  cons TEXT,
  source TEXT,
  raw_json JSONB
);

CREATE TABLE fetch_audit (
  id BIGSERIAL PRIMARY KEY,
  hotel_id BIGINT,
  last_property_fetch TIMESTAMP WITH TIME ZONE,
  last_translation_fetch TIMESTAMP WITH TIME ZONE,
  last_review_fetch TIMESTAMP WITH TIME ZONE,
  status TEXT
);
