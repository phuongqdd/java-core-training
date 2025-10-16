package com.dophuong.producer;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RiderLocation {
    private String riderId;
    private double latitude;
    private double longitude;
}
