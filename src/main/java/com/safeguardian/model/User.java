package com.safeguardian.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User {

    private String id;
    private String fullName;
    private String phone;
    private String email;
    private String bloodType;
    private String dateOfBirth;
    private String gender;
    private String medicalNote;
    private String address;

    /** ACTIVE | INACTIVE | BLOCKED */
    private String status;

    private String createdAt;
    private String updatedAt;
}
