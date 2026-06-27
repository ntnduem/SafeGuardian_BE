package vn.edu.hcmuaf.fit.safeguardian.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import vn.edu.hcmuaf.fit.safeguardian.constant.Gender;

@Data
public class UserCreateRequest {
    @NotBlank
    private String fullName;
    @NotBlank
    private String phone;
    @Email
    private String email;
    private String bloodType;
    private String dateOfBirth;
    private Gender gender;
    private String medicalNote;
    private String address;
}
