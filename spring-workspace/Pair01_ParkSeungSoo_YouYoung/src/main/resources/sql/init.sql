USE enjoytrips;

DROP TABLE IF EXISTS member;
DROP TABLE IF EXISTS membersec;

CREATE TABLE member(
	id VARCHAR(50) PRIMARY KEY,
    password VARCHAR(100) NOT NULL,
    name VARCHAR(50) NOT NULL,
    email VARCHAR(100) NOT NULL,
    sido_code int,
    gugun_code int,
    created_at TIMESTAMP,
    
    FOREIGN KEY (sido_code) REFERENCES sido(sido_code),
    FOREIGN KEY (gugun_code) REFERENCES gugun(gugun_code)
);

CREATE TABLE membersec(
	id VARCHAR(50) PRIMARY KEY,
    salt VARCHAR(255) NOT NULL,
    sec_key VARCHAR(255) NOT NULL,
    
    FOREIGN KEY (id) REFERENCES member(id)
);

CREATE TABLE trip_type (
  content_code int PRIMARY KEY,
  content_name VARCHAR(50) NOT NULL
);

INSERT INTO trip_type (content_code, content_name)
VALUES (12, '관광지'),(14, '문화시설'),(15, '축제공연행사'),(25, '여행코스'),(28, '레포츠'),(32, '숙박'),(38, '쇼핑'),(39, '음식점');


CREATE TABLE location_board (
  code int AUTO_INCREMENT PRIMARY KEY,
  title VARCHAR(50) NOT NULL,
  contents VARCHAR(50) NOT NULL,
    writer_id VARCHAR(50) NOT NULL,
    latitude DECIMAL(20, 17) NOT NULL,
    longitude DECIMAL(20, 17) NOT NULL,
    created_at TIMESTAMP,
    
    FOREIGN KEY (writer_id) REFERENCES member(id)
);

CREATE TABLE location_board (
  code int AUTO_INCREMENT PRIMARY KEY,
  title VARCHAR(50) NOT NULL,
  contents VARCHAR(50) NOT NULL,
    writer_id VARCHAR(50) NOT NULL,
    
    created_at TIMESTAMP,
    
    FOREIGN KEY (writer_id) REFERENCES member(id)
);

CREATE TABLE qna_board (
  code int AUTO_INCREMENT PRIMARY KEY,
  title VARCHAR(50) NOT NULL,
  contents VARCHAR(50) NOT NULL,
    writer_id VARCHAR(50) NOT NULL,
    created_at TIMESTAMP,
    
    FOREIGN KEY (writer_id) REFERENCES member(id)
);