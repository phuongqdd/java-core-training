use ex;

-- Use the USING keyword to join three tables
SELECT department_name, last_name, project_number
FROM departments
	JOIN employees USING (department_number)
	LEFT JOIN projects USING (employee_id )
ORDER BY department_name;

-- Use the NATURAL keyword in a statement that joins three tables
SELECT department_name AS dept_name, last_name, project_number
FROM departments
	NATURAL JOIN employees
	LEFT JOIN projects USING (employee_id)
ORDER BY department_name;

-- Ví dụ UNION hai bảng
SELECT 'Active' AS source, invoice_number, invoice_date, invoice_total
FROM active_invoices
WHERE invoice_date >= '2018-06-01'
UNION
SELECT 'Paid' AS source, invoice_number, invoice_date, invoice_total
FROM paid_invoices
WHERE invoice_date >= '2018-06-01'
ORDER BY invoice_total DESC;

-- A union that simulates a full outer join
SELECT department_name AS dept_name, d.department_number AS d_dept_no,
       e.department_number AS e_dept_no, last_name
FROM departments d
LEFT JOIN employees e
ON d.department_number = e.department_number

UNION

SELECT department_name AS dept_name, d.department_number AS d_dept_no,
       e.department_number AS e_dept_no, last_name
FROM departments d
RIGHT JOIN employees e
ON d.department_number = e.department_number
ORDER BY dept_name;


use ap;
-- UNION kết hợp result set từ cùng một bảng
SELECT 'Active' AS source, invoice_number, invoice_date, invoice_total
FROM invoices
WHERE invoice_total - payment_total - credit_total > 0
UNION
SELECT 'Paid' AS source, invoice_number, invoice_date, invoice_total
FROM invoices
WHERE invoice_total - payment_total - credit_total <= 0
ORDER BY invoice_total DESC;

-- từ hai bảng Invoices và Vendors
SELECT invoice_number, vendor_name, '33% Payment' AS payment_type,
       invoice_total AS total, invoice_total * 0.333 AS payment
FROM invoices
JOIN vendors ON invoices.vendor_id = vendors.vendor_id
WHERE invoice_total > 10000

UNION

SELECT invoice_number, vendor_name, '50% Payment' AS payment_type,
       invoice_total AS total, invoice_total * 0.5 AS payment
FROM invoices
JOIN vendors ON invoices.vendor_id = vendors.vendor_id
WHERE invoice_total BETWEEN 500 AND 10000

UNION

SELECT invoice_number, vendor_name, 'Full amount' AS payment_type,
       invoice_total AS total, invoice_total AS payment
FROM invoices
JOIN vendors ON invoices.vendor_id = vendors.vendor_id
WHERE invoice_total < 500
ORDER BY payment_type, vendor_name, invoice_number;


