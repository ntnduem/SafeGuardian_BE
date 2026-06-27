package vn.edu.hcmuaf.fit.safeguardian.dto;

import jakarta.validation.constraints.Email;
import lombok.Data;
import vn.edu.hcmuaf.fit.safeguardian.constant.Gender;

@Data
public class UserUpdateRequest {
    private String fullName;
    private String phone;
    @Email
    private String email;
    private String bloodType;
    private String dateOfBirth;
    private Gender gender;
    private String medicalNote;
    private String address;
}
