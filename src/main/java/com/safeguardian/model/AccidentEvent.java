package com.safeguardian.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AccidentEvent {

    private String id;
    private String userId;
    private String deviceId;

    /** DROP | STRONG_IMPACT | SIMULATION */
    private String eventType;

    private Double acceleration;
    private Double threshold;
    private Double latitude;
    private Double longitude;

    /** true nếu người dùng xác nhận đây là tai nạn thật */
    private Boolean isConfirmedAccident;

    private String createdAt;
}
