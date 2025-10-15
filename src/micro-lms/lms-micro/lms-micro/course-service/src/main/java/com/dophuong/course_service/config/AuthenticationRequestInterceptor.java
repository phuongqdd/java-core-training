package com.dophuong.course_service.config;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Slf4j
public class AuthenticationRequestInterceptor implements RequestInterceptor {

    @Override
    public void apply(RequestTemplate template) {
        // Lấy thông tin request hiện tại (người dùng đang gọi đến service này)
        ServletRequestAttributes servletRequestAttributes =
                (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();

        if (servletRequestAttributes == null) {
            log.warn("Không tìm thấy request hiện tại");
            return;
        }

        HttpServletRequest request = servletRequestAttributes.getRequest();

        // Lấy header Authorization từ request gốc
        String authHeader = request.getHeader("Authorization");

        log.info("Header Authorization nhận được: {}", authHeader);

        // Nếu có token thì gắn nó vào request gửi sang service khác (qua Feign)
        if (StringUtils.hasText(authHeader)) {
            template.header("Authorization", authHeader);
            log.info("Đã gắn token Authorization vào request gửi đi");
        } else {
            log.warn("Không có token Authorization trong request hiện tại");
        }
    }
}

