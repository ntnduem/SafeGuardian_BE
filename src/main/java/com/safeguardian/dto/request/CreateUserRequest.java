package com.safeguardian.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CreateUserRequest {

    @NotBlank(message = "Họ tên không được để trống")
    private String fullName;

    @NotBlank(message = "Số điện thoại không được để trống")
    private String phone;

    private String email;
    private String bloodType;
    private String dateOfBirth;
    private String gender;
    private String medicalNote;
    private String address;
}
