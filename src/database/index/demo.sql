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









