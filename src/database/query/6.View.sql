use ap;

CREATE VIEW v_invoices_simple AS
SELECT invoice_id, vendor_id, invoice_number, invoice_total, payment_date
FROM invoices;

SELECT * FROM v_invoices_simple;

CREATE OR REPLACE VIEW v_invoices_simple AS
SELECT invoice_id, vendor_id, invoice_number, invoice_total, payment_date, terms_id
FROM invoices;

INSERT INTO v_invoices_simple (vendor_id, invoice_number, invoice_total, payment_date, terms_id)
VALUES (101, 'INV-001', 500.00, '2025-09-04', 1);

UPDATE v_invoices_simple
SET invoice_total = 2000.00
WHERE vendor_id = 101;

DELETE FROM v_invoices_simple WHERE vendor_id = 101;


-- Trường hợp CUD không được
CREATE VIEW v_invoices_summary AS
SELECT vendor_id, SUM(invoice_total) AS total_amount
FROM invoices
GROUP BY vendor_id;

SELECT * FROM v_invoices_summary;
INSERT INTO v_invoices_summary (vendor_id, total_amount)
VALUES (102, 1000.00);

UPDATE v_invoices_summary
SET total_amount = 2000.00
WHERE vendor_id = 101;

DELETE FROM v_invoices_summary WHERE vendor_id = 101;








