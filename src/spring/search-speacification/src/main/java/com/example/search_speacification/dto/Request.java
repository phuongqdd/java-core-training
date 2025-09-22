package com.example.search_speacification.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class Request {
    private SearchRequest request;
}
