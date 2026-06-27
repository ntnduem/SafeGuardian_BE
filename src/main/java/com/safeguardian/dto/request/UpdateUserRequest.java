package com.safeguardian.dto.request;

import lombok.Data;

@Data
public class UpdateUserRequest {

    private String fullName;
    private String phone;
    private String email;
    private String bloodType;
    private String dateOfBirth;
    private String gender;
    private String medicalNote;
    private String address;
}
