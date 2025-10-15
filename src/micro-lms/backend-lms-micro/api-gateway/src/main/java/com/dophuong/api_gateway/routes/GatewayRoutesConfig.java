package com.dophuong.api_gateway.routes;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GatewayRoutesConfig {

    @Bean
    public RouteLocator customRoutes(RouteLocatorBuilder builder) {
        return builder.routes()
                .route("quiz-service", r -> r
                        .path("/courses/{courseId}/quizzes/**")
                        .uri("lb://QUIZ-SERVICE"))

                .route("question-service", r -> r
                        .path("/courses/{courseId}/questions/**",
                                "/course/{courseId}/questions/{questionId}/history/**")
                        .uri("lb://QUESTION-SERVICE"))

                .route("identity-service", r -> r
                        .path("/auth/**")
                        .uri("lb://IDENTITY-SERVICE"))

                .route("course-service", r -> r
                        .path("/courses/**")
//                        .filters(f -> f.addRequestHeader("X-Internal-Call", "secret-token"))
                        .uri("lb://COURSE-SERVICE"))

                .build();
    }
}