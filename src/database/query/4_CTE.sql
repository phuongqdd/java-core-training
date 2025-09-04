use ap;

-- Hai CTE và một truy vấn sử dụng chúng
WITH summary AS
(
    SELECT vendor_state, vendor_name, SUM(invoice_total) AS sum_of_invoices
    FROM vendors v JOIN invoices i
    ON v.vendor_id = i.vendor_id
    GROUP BY vendor_state, vendor_name
),
top_in_state AS
(
    SELECT vendor_state, MAX(sum_of_invoices) AS sum_of_invoices
    FROM summary
    GROUP BY vendor_state
)
SELECT summary.vendor_state, summary.vendor_name,
       top_in_state.sum_of_invoices
FROM summary JOIN top_in_state
ON summary.vendor_state = top_in_state.vendor_state
   AND summary.sum_of_invoices = top_in_state.sum_of_invoices
ORDER BY summary.vendor_state;

use ex;
WITH RECURSIVE employees_cte AS (
    -- Non-recursive: chọn manager cấp cao nhất (không có manager)
    SELECT employee_id,
           CONCAT(first_name, ' ', last_name) AS employee_name,
           1 AS ranking
    FROM employees
    WHERE manager_id IS NULL

    UNION ALL

    -- Recursive: tìm nhân viên cấp dưới
    SELECT e.employee_id,
           CONCAT(e.first_name, ' ', e.last_name),
           c.ranking + 1
    FROM employees e
    JOIN employees_cte c
    ON e.manager_id = c.employee_id
)
SELECT *
FROM employees_cte
ORDER BY ranking, employee_id;

