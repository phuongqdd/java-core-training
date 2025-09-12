use ap;

-- kiểm tra thông tin bảng
desc vendors;

-- Xem index hiện có
SHOW INDEXES FROM vendors;

EXPLAIN SELECT * FROM vendors WHERE vendor_id = 10;

EXPLAIN SELECT * FROM vendors WHERE vendor_name = 'IBM';

EXPLAIN 
SELECT v.vendor_id, t.terms_description
FROM vendors v
JOIN terms t ON v.default_terms_id = t.terms_id;

EXPLAIN SELECT * FROM vendors WHERE vendor_state = 'CA';

CREATE INDEX idx_vendor_state ON vendors(vendor_state);

EXPLAIN SELECT * FROM vendors WHERE vendor_state = 'CA';

EXPLAIN SELECT vendor_name FROM vendors WHERE vendor_name = 'IBM';

EXPLAIN SELECT * FROM vendors WHERE vendor_name = 'IBM';

CREATE DATABASE IF NOT EXISTS bigdata_demo;
USE bigdata_demo;


CREATE TABLE users (
    user_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(100) NOT NULL,
    full_name VARCHAR(100) NOT NULL,
    email VARCHAR(150) NOT NULL,
    phone VARCHAR(20),
    city VARCHAR(100),
    created_at DATETIME
);

LOAD DATA INFILE 'D:/TaiLieu/LapTrinhPython/LapTrinhNangCao/Docfile/users.csv'
INTO TABLE users
FIELDS TERMINATED BY ','
ENCLOSED BY '"'
LINES TERMINATED BY '\n'
(username, full_name, email, phone, city, created_at);


select user_id, username, full_name, email, phone, city FROm users;

SELECT *
FROM users 
WHERE user_id BETWEEN 9000000 AND 9001000;

SELECT *
FROM users
WHERE CAST(SUBSTRING(username, 5) AS UNSIGNED) BETWEEN 9000000 AND 9001000;

SELECT *
FROM users
WHERE username = 'user5678345';

create index id1user on users(username);










