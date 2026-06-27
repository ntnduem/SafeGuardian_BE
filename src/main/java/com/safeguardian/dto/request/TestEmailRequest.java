package com.safeguardian.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class TestEmailRequest {

    @NotBlank(message = "Địa chỉ email không được để trống")
    @Email(message = "Email không hợp lệ")
    private String to;

    private String subject;
    private String content;
}
