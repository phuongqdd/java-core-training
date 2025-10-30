package com.dophuong.notification_email_svc.template;

import com.dophuong.notification_email_svc.model.OrderEvent;

public class EmailTemplates {
    public static String orderCreated(OrderEvent e) {
        return """
              <div style="font-family:Arial,sans-serif">
                <h3>Đơn hàng mới: %s</h3>
                <p>Đơn của bạn đã được tạo.</p>
                <ul>
                  <li>Tổng tiền: %s %s</li>
                  <li>Trạng thái: %s</li>
                  <li>Thời gian: %s</li>
                </ul>
              </div>
      """.formatted(e.getOrderId(), e.getTotal(), orDash(e.getCurrency()), orDash(e.getStatus()), e.getEventTime());
    }
    public static String statusChanged(OrderEvent e) {
        return """
              <div style="font-family:Arial,sans-serif">
                <h3>Cập nhật đơn: %s</h3>
                <p>%s → <b>%s</b></p>
              </div>
      """.formatted(e.getOrderId(), orDash(e.getPreviousStatus()), orDash(e.getStatus()));
    }
    private static String orDash(String s){
        return s==null?"-":s;
    }
}
