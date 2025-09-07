use ap;

USE ap;

CREATE TABLE invoices_partitioned (
    invoice_id INT NOT NULL AUTO_INCREMENT,
    vendor_id INT,
    invoice_number VARCHAR(50),
    invoice_date DATE NOT NULL,
    invoice_total DECIMAL(9,2),
    payment_total DECIMAL(9,2),
    credit_total DECIMAL(9,2),
    terms_id INT,
    invoice_due_date DATE,
    payment_date DATE,
    PRIMARY KEY (invoice_id, invoice_date)
)
PARTITION BY RANGE (YEAR(invoice_date)) (
    PARTITION p2019 VALUES LESS THAN (2020),
    PARTITION p2020 VALUES LESS THAN (2021),
    PARTITION p2021 VALUES LESS THAN (2022),
    PARTITION p2022 VALUES LESS THAN (2023),
    PARTITION p2023 VALUES LESS THAN (2024),
    PARTITION p2024 VALUES LESS THAN (2025),
    PARTITION pmax  VALUES LESS THAN MAXVALUE
);

INSERT INTO invoices_partitioned
(vendor_id, invoice_number, invoice_date, invoice_total, payment_total, credit_total, terms_id, invoice_due_date, payment_date)
VALUES
(1, 'INV-2019-001', '2019-06-10', 500.00, 500.00, 0.00, 1, '2019-07-10', '2019-07-01'),
(2, 'INV-2020-045', '2020-08-15', 1200.00, 0.00, 0.00, 2, '2020-09-15', NULL),
(3, 'INV-2021-088', '2021-02-20', 300.00, 300.00, 0.00, 1, '2021-03-20', '2021-03-05'),
(4, 'INV-2022-150', '2022-11-11', 750.00, 200.00, 0.00, 3, '2022-12-11', '2022-12-01'),
(5, 'INV-2023-222', '2023-01-09', 900.00, 900.00, 0.00, 2, '2023-02-09', '2023-02-05');

EXPLAIN PARTITIONS
SELECT invoice_id, invoice_number, invoice_total
FROM invoices_partitioned
WHERE invoice_date BETWEEN '2021-01-01' AND '2021-12-31';




