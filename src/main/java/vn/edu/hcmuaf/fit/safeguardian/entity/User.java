package vn.edu.hcmuaf.fit.safeguardian.entity;

import java.time.Instant;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import vn.edu.hcmuaf.fit.safeguardian.constant.Gender;
import vn.edu.hcmuaf.fit.safeguardian.constant.UserStatus;

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
    private Gender gender;
    private String medicalNote;
    private String address;
    private UserStatus status;
    private Instant createdAt;
    private Instant updatedAt;
}
