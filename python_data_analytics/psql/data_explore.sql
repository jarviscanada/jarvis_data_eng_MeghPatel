-- Show table schema 
\d+ retail;

-- Show first 10 rows
Select * from retail limit 10;

-- Check # of records
Select count(*) from retail;

-- number of clients (e.g. unique client ID)
Select Count(Distinct(customer_id)) from retail;

-- invoice date range (e.g. max/min dates)
Select Max(invoice_date), Min(invoice_date) from retail;

-- number of SKU/merchants (e.g. unique stock code)
Select Count(Distinct(stock_code)) from retail;

-- Calculate average invoice amount excluding invoices with a negative amount (e.g. canceled orders have negative amount)
SELECT AVG(invoice_total) 
FROM (
    SELECT invoice_no, SUM(quantity * unit_price) AS invoice_total
    FROM retail
    GROUP BY invoice_no
    HAVING SUM(quantity * unit_price) > 0
) AS subquery;

-- Calculate total revenue (e.g. sum of unit_price * quantity)
Select Sum(unit_price * quantity) from retail;

-- Calculate total revenue by YYYYMM
SELECT 
    to_char(invoice_date, 'YYYYMM') AS yyyymm,
    SUM(quantity * unit_price) AS total_revenue
FROM retail
GROUP BY yyyymm
HAVING SUM(quantity * unit_price) > 0
ORDER BY yyyymm;
