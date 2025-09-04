use ap;

DELIMITER $$
CREATE PROCEDURE KiemTraHoaDon()
BEGIN
	DECLARE SoLuongHoaDon INT DEFAULT 0;
    DECLARE TongNo DECIMAL(10, 2) DEFAULT 0.00;
    
    -- Tính số lượng hóa đơn còn nợ
    SElECT COUNT(*) INTO SoLuongHoaDon
    FROM invoices
    where invoice_total - payment_total - credit_total > 0;
    
    select SUM(invoice_total - payment_total - credit_total) INTO TongNo
    from invoices
    where invoice_total - payment_total - credit_total > 0;
    
    IF TongNo >= 30000 THEN
		SELECT SoLuongHoaDon AS 'Số lượng hóa đơn còn nợ', 
               TongNo AS 'Tổng số dư còn nợ';
    ELSE
		SELECT 'Tổng số dư còn nợ nhỏ hơn $30,000' AS Thông_báo;
    END IF;
END$$

DELIMITER ;
CALL KiemTraHoaDon();