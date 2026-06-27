package vn.edu.hcmuaf.fit.safeguardian.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class AccidentAlertRequest {
    @NotBlank
    private String userId;
    @NotBlank
    private String eventId;
    @NotNull
    private Double latitude;
    @NotNull
    private Double longitude;
    private Double acceleration;
}
