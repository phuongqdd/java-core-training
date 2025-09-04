use ap;

-- A query that uses the ROW_NUMBER function
SELECT ROW_NUMBER() OVER (ORDER BY vendor_name) AS "row_number",
       vendor_name
FROM vendors;

-- có partition
SELECT ROW_NUMBER() OVER (PARTITION BY vendor_state ORDER BY vendor_name) AS 'row_number',
       vendor_name, vendor_state
FROM vendors;

-- A query that uses the RANK and DENSE_RANK functions
SELECT 
    RANK() OVER (ORDER BY invoice_total) AS 'rank',
    DENSE_RANK() OVER (ORDER BY invoice_total) AS 'dense_rank',
    invoice_total, invoice_number
FROM invoices;


