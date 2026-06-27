package vn.edu.hcmuaf.fit.safeguardian.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ContactRequest {
    @NotBlank
    private String fullName;
    @NotBlank
    private String relationship;
    @NotBlank
    private String phone;
    @Email
    private String email;
    @NotNull
    @Min(1)
    private Integer priority;
    private Boolean isPrimary;
}
