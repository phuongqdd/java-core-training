package com.dophuong.notification_email_svc.controller;

import com.dophuong.notification_email_svc.service.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/_health")
class MailTestController {
    private final EmailService emailService;

    @GetMapping("/test-mail")
    public String test(@RequestParam String to) {
        emailService.sendHtml(to, "[SMTP OK] Gmail App Password",
                "<h3>Xin chào</h3><p>Test gửi email thật qua Gmail SMTP.</p>");
        return "Sent to " + to;
    }
}
