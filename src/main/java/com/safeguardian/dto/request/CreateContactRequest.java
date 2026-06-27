package com.safeguardian.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CreateContactRequest {

    @NotBlank(message = "Họ tên không được để trống")
    private String fullName;

    private String relationship;

    @NotBlank(message = "Số điện thoại không được để trống")
    private String phone;

    private String email;
    private Integer priority;
    private Boolean isPrimary;
}
