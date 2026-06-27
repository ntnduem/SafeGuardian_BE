package vn.edu.hcmuaf.fit.safeguardian.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class AccidentConfirmRequest {
    @NotNull
    private Boolean isConfirmedAccident;
}
