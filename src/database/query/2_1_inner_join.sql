use ap;

-- An inner join of the Vendors and Invoices tables
SELECT invoice_number, vendor_name
FROM vendors INNER JOIN invoices
	ON vendors.vendor_id = invoices.vendor_id
ORDER BY invoice_number;

-- Aliases for all tables
SELECT invoice_number, vendor_name, invoice_due_date,
	invoice_total - payment_total - credit_total AS balance_due
FROM vendors v JOIN invoices i
ON v.vendor_id = i.vendor_id
WHERE invoice_total - payment_total - credit_total> 0
ORDER BY balance_due DESC;

-- tìm các vendor mà thành phố của họ cũng có ít nhất một vendor khác.
SELECT DISTINCT vl.vendor_name, vl.vendor_city,
	vl.vendor_state
FROM vendors vl JOIN vendors v2
ON vl.vendor_city = v2.vendor_city AND
	vl.vendor_state = v2.vendor_state AND
	vl.vendor_name <> v2.vendor_name
ORDER BY vl.vendor_state, vl.vendor_city;

-- A statement that joins four tables
SELECT vendor_name, invoice_number, invoice_date,
	line_item_amount, account_description
FROM vendors v JOIN invoices i
		ON v.vendor_id = i.vendor_id
	JOIN invoice_line_items li
		ON i.invoice_id = li.invoice_id
	JOIN general_ledger_accounts gl
		ON li.account_number = gl.account_number
WHERE invoice_total - payment_total - credit_total > 0
ORDER BY vendor_name, line_item_amount DESC;

-- Join four tables
SELECT vendor_name, invoice_number, invoice_date,
	line_item_amount, account_description
FROM vendors v, invoices i, invoice_line_items li,
	general_ledger_accounts gl
WHERE v.vendor_id= i.vendor_id
	AND i.invoice_id = li.invoice_id
	AND li. account_number = gl. account_number
	AND invoice_total - payment_total - credit_total > 0
ORDER BY vendor_name, line_item_amount DESC;