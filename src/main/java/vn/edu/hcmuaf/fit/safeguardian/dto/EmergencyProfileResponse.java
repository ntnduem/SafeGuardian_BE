package vn.edu.hcmuaf.fit.safeguardian.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class EmergencyProfileResponse {
    private String fullName;
    private String phone;
    private String bloodType;
    private String medicalNote;
    private String address;
    private PrimaryContact primaryContact;

    @Data
    @Builder
    public static class PrimaryContact {
        private String fullName;
        private String relationship;
        private String phone;
        private String email;
    }
}
