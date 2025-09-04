use ap;

-- A left outer join
SELECT vendor_name, invoice_number, invoice_total
FROM vendors LEFT JOIN invoices
	ON vendors.vendor_id = invoices.vendor_id
ORDER BY vendor_name;

use ex;

-- A left outer join
SELECT department_name, d.department_number, last_name
FROM departments d LEFT JOIN employees e
	ON d.department_number = e.department_number
ORDER BY department_name;

-- A right outer join
SELECT department_name, e.department_number, last_name
FROM departments d RIGHT JOIN employees e
	ON d.department_number = e.department_number
ORDER BY department_name;

-- Join three tables using left outer joins
SELECT department_name, last_name, project_number
FROM departments d LEFT JOIN employees e
		ON d.department_number = e.department_number
	LEFT JOIN projects p
		ON e.employee_id = p.employee_id
ORDER BY department_name, last_name;

-- Combine an outer and an inner join
SELECT department_name, last_name, project_number
FROM departments d JOIN employees e
		ON d.department_number = e.department_number
	LEFT JOIN projects p
		ON e.employee_id = p.employee_id
ORDER BY department_name, last_name;