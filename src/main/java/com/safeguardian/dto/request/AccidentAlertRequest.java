package com.safeguardian.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class AccidentAlertRequest {

    @NotBlank(message = "userId không được để trống")
    private String userId;

    private String eventId;
    private Double latitude;
    private Double longitude;
    private Double acceleration;
}
