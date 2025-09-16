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

create index id2user on users(email);

create index id3user on users(phone);

create index id4user on users(city);

select username, full_name
from users
where username > "user5678345" and email < "user5678390@example.com" and phone between "+84681623456" and "+84689623456";

explain select username, full_name
from users
where username > "user5678345" and email < "user5678390@example.com" and phone between "+84681623456" and "+84689623456";

select username, full_name
from users
where email < "user5678390@@example.com" and phone between "+84681623456" and "+84689623456" and username > "user5678345" ;

select username, full_name
from users
where phone between "+84681623456" and "+84689623456" and email < "user5678390@example.com" and  username > "user5678345" ;

select user_id, username, full_name, email, phone, city, created_at
from users
where phone = "+84629583606" and email = "user8976530@example.com" and  username = "user8976530" ;

select user_id, username, full_name, email, phone, city, created_at
from users
where city = "HCM" AND username like "user84%" ;

select user_id, username, full_name, email, phone, city, created_at
from users
where city = "HCM" AND username like "user84%" and full_name like "Huỳnh Quốc Linh";

select *
from users
limit 8976456, 100;


CREATE INDEX idx_users_username_email_phone
ON users (username, email, phone);


select * from users_test;

select count(*) from users_test;

select count(id) from users_test;

EXPLAIN SELECT * FROM users_test WHERE full_name LIKE '%John%';

EXPLAIN SELECT * FROM users_test WHERE username = 'user1234567';

-- Index đơn
CREATE INDEX idx_username ON users_test(username);
CREATE INDEX idx_email ON users_test(email);
CREATE INDEX idx_phone ON users_test(phone);
CREATE INDEX idx_created_at ON users_test(created_at);

-- Composite index
CREATE INDEX idx_username_email ON users_test(username, email);
CREATE INDEX idx_username_email_phone ON users_test(username, email, phone);

-- Fulltext index (áp dụng cho text search)
CREATE FULLTEXT INDEX idx_fullname ON users_test(full_name);
EXPLAIN SELECT * FROM users_test WHERE username = 'user1234567';
SELECT * FROM users_test WHERE username = 'user1234567';

EXPLAIN SELECT * FROM users_test 
WHERE phone BETWEEN '+84680000000' AND '+84689999999';

-- Dùng được index (prefix, không có % ở đầu)
EXPLAIN SELECT * FROM users_test WHERE email LIKE 'user12%';
SELECT * FROM users_test WHERE email LIKE 'user12%';

-- Không dùng được index (có % ở đầu)
EXPLAIN SELECT * FROM users_test WHERE email LIKE '%example.com';

EXPLAIN SELECT * FROM users_test 
WHERE username = 'user12345' 
  AND email = 'user12345@example.com';
  
EXPLAIN SELECT * FROM users_test 
WHERE username > 'user1000' 
  AND email < 'user9000@example.com';
  
EXPLAIN SELECT * FROM users_test 
WHERE email = 'user123@example.com' AND phone = '+84912345678';

EXPLAIN SELECT * FROM users_test 
WHERE MATCH(full_name) AGAINST ('John' IN NATURAL LANGUAGE MODE);

-- Index (username, email) có thể cover query này
SELECT username, email FROM users_test 
WHERE username = 'user9103489';
EXPLAIN SELECT username, email FROM users_test 
WHERE username = 'user9103489';

explain select username FROM users_test
WHERE email = 'user@example.com' 
  AND phone = '+84912345678';
  
SELECT email FROM users_test
WHERE email = 'user@example.com' 
  AND phone = '+84912345678';
-- 'user9103489', 'user9103489@example.com', '+84801021531'

select username, email, phone from users_test limit 900000, 20;




select * from users_test;







