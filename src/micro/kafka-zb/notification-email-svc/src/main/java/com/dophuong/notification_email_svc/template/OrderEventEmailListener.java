package com.dophuong.notification_email_svc.template;

import com.dophuong.notification_email_svc.model.OrderEvent;
import com.dophuong.notification_email_svc.service.EmailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.header.Header;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;

@Component
@RequiredArgsConstructor
@Slf4j
public class OrderEventEmailListener {
    private final EmailService emailService;

    @KafkaListener(topics = "order.events")
    public void onMessage(ConsumerRecord<String, OrderEvent> r, Acknowledgment ack) {
        OrderEvent e = r.value();
        String type = header(r, "eventType"); // ORDER_CREATED / ORDER_STATUS_CHANGED

        try {
            if (e.getEmail()==null || e.getEmail().isBlank()) { ack.acknowledge(); return; }

            if ("ORDER_CREATED".equals(type) || (type==null && e.getPreviousStatus()==null)) {
                emailService.sendHtml(e.getEmail(), "[Demo] Đơn mới " + e.getOrderId(), EmailTemplates.orderCreated(e));
            } else if ("ORDER_STATUS_CHANGED".equals(type) || (type==null && e.getPreviousStatus()!=null)) {
                emailService.sendHtml(e.getEmail(), "[Demo] Cập nhật " + e.getOrderId(), EmailTemplates.statusChanged(e));
            } else {
                log.warn("Unknown eventType={}, orderId={}", type, e.getOrderId());
            }

            ack.acknowledge();
        } catch (Exception ex) {
            log.error("Send email failed, orderId={}", e.getOrderId(), ex);
            ack.acknowledge(); // test: tránh kẹt partition
        }
    }

    private String header(ConsumerRecord<?,?> r, String key) {
        Header h = r.headers().lastHeader(key);
        return h==null? null : new String(h.value(), StandardCharsets.UTF_8);
    }
}