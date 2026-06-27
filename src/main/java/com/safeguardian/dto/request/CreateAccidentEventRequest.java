package com.safeguardian.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CreateAccidentEventRequest {

    @NotBlank(message = "userId không được để trống")
    private String userId;

    private String eventType;
    private Double acceleration;
    private Double threshold;
    private Double latitude;
    private Double longitude;
    private Boolean isConfirmedAccident;
}
