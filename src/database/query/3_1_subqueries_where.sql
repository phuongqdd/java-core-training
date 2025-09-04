use ap;

-- Lấy các nhà cung cấp (Vendors) không có hóa đơn (Invoices):
SELECT vendor_id, vendor_name, vendor_state
FROM vendors
WHERE vendor_id NOT IN (
    SELECT DISTINCT vendor_id
    FROM invoices
)
ORDER BY vendor_id;


-- Lấy các hóa đơn có số dư chưa thanh toán (balance_due) nhỏ hơn mức trung bình:
SELECT invoice_number, invoice_date,
       invoice_total - payment_total - credit_total AS balance_due
FROM invoices
WHERE invoice_total - payment_total - credit_total > 0
AND invoice_total - payment_total - credit_total <
(
  SELECT AVG(invoice_total - payment_total - credit_total)
  FROM invoices
  WHERE invoice_total - payment_total - credit_total > 0
)
ORDER BY invoice_total DESC;

-- Lấy các hóa đơn có tổng lớn hơn hóa đơn lớn nhất của vendor 34:
SELECT vendor_name, invoice_number, invoice_total
FROM invoices i 
JOIN vendors v ON i.vendor_id = v.vendor_id
WHERE invoice_total > ALL
(
  SELECT invoice_total
  FROM invoices
  WHERE vendor_id = 34
)
ORDER BY vendor_name;

-- Lấy các hóa đơn nhỏ hơn hóa đơn lớn nhất của vendor 115:
SELECT vendor_name, invoice_number, invoice_total
FROM vendors
JOIN invoices ON vendors.vendor_id = invoices.vendor_id
WHERE invoice_total < ANY
(
  SELECT invoice_total
  FROM invoices
  WHERE vendor_id = 115
);

-- lấy các hóa đơn có tổng lớn hơn trung bình hóa đơn cùng vendor:
SELECT vendor_id, invoice_number, invoice_total
FROM invoices i
WHERE invoice_total > (
    SELECT AVG(invoice_total)
    FROM invoices
    WHERE vendor_id = i.vendor_id
)
ORDER BY vendor_id, invoice_total;


-- Lấy các vendor không có hóa đơn:
SELECT vendor_id, vendor_name, vendor_state
FROM vendors
WHERE NOT EXISTS (
    SELECT *
    FROM invoices
    WHERE vendor_id = vendors.vendor_id
);

-- Truy vấn con tính ngày hóa đơn mới nhất (MAX(invoice_date)) cho từng vendor.
SELECT vendor_name,
       (SELECT MAX(invoice_date) 
        FROM invoices
        WHERE vendor_id = vendors.vendor_id) AS latest_inv
FROM vendors
ORDER BY latest_inv DESC;

-- Lấy tổng hóa đơn lớn nhất cho vendor đứng đầu mỗi tiểu bang
SELECT vendor_state, 
       MAX(sum_of_invoices) AS max_sum_of_invoices
FROM
(
    SELECT vendor_state, vendor_name,
           SUM(invoice_total) AS sum_of_invoices
    FROM vendors v 
    JOIN invoices i ON v.vendor_id = i.vendor_id
    GROUP BY vendor_state, vendor_name
) t
GROUP BY vendor_state
ORDER BY vendor_state;


