package com.dophuong.notification_email_svc.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderEvent {
    private String eventId;
    private Instant eventTime;
    private String orderId;
    private String userId;
    private String email;
    private String phone;          // không dùng, giữ schema
    private String status;
    private String previousStatus; // null nếu là ORDER_CREATED
    private Long total;
    private String currency;
    private String locale;
    private Map<String, Object> metadata;
    private String traceId;
}