package vn.edu.hcmuaf.fit.safeguardian.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import vn.edu.hcmuaf.fit.safeguardian.constant.AccidentEventType;

@Data
public class AccidentEventCreateRequest {
    @NotBlank
    private String userId;
    @NotNull
    private AccidentEventType eventType;
    @NotNull
    private Double acceleration;
    @NotNull
    private Double threshold;
    @NotNull
    private Double latitude;
    @NotNull
    private Double longitude;
    private Boolean isConfirmedAccident;
}
