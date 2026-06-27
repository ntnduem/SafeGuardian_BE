package com.safeguardian.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EmergencyContact {

    private String id;
    private String userId;
    private String fullName;

    /** Father | Mother | Brother | Sister | Friend | Other */
    private String relationship;

    private String phone;
    private String email;
    private Integer priority;

    /** Dùng Boolean (wrapper) để Firestore serialize đúng tên field */
    private Boolean isPrimary;

    private String createdAt;
    private String updatedAt;
}
