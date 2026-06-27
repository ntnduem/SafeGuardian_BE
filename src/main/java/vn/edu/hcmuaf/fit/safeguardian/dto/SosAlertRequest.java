package vn.edu.hcmuaf.fit.safeguardian.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class SosAlertRequest {
    @NotBlank
    private String userId;
    @NotNull
    private Double latitude;
    @NotNull
    private Double longitude;
}
