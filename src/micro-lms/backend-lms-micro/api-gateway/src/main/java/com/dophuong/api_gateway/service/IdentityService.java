package com.dophuong.api_gateway.service;

import com.dophuong.api_gateway.dto.request.IntrospectRequest;
import com.dophuong.api_gateway.dto.response.ApiResponse;
import com.dophuong.api_gateway.dto.response.IntrospectResponse;
import com.dophuong.api_gateway.repository.IdentityClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class IdentityService {

    private final IdentityClient identityClient;

    public Mono<ApiResponse<IntrospectResponse>> introspect(String token){
        return identityClient.introspect(IntrospectRequest.builder().token(token).build());
    }

}
