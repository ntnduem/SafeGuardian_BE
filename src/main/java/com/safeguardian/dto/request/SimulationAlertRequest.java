package com.safeguardian.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class SimulationAlertRequest {

    @NotBlank(message = "userId không được để trống")
    private String userId;

    private Double latitude;
    private Double longitude;
}
