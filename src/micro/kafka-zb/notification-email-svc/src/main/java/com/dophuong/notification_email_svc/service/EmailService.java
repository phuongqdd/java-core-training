package com.dophuong.notification_email_svc.service;

import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailService {
    private final JavaMailSender mailSender;
    @Value("${app.email.from}") private String from;
    @Value("${app.email.enabled:true}") private boolean enabled;

    public void sendHtml(String to, String subject, String html) {
        if (!enabled) { log.info("[DRY-RUN] Email -> {} | {}", to, subject); return; }
        try {
            MimeMessage msg = mailSender.createMimeMessage();
            MimeMessageHelper h = new MimeMessageHelper(msg, true, "UTF-8");
            h.setFrom(from); h.setTo(to); h.setSubject(subject); h.setText(html, true);
            mailSender.send(msg);
            log.info("Sent email -> {}", to);
        } catch (Exception e) {
            throw new RuntimeException("Send email failed: " + e.getMessage(), e);
        }
    }
}
