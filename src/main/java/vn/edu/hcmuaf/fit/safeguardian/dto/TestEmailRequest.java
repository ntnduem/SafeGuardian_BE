package vn.edu.hcmuaf.fit.safeguardian.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class TestEmailRequest {
    @NotBlank
    @Email
    private String to;
    @NotBlank
    private String subject;
    @NotBlank
    private String content;
}
