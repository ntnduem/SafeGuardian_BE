package vn.edu.hcmuaf.fit.safeguardian.entity;

import java.time.Instant;

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
    private String relationship;
    private String phone;
    private String email;
    private Integer priority;
    private Boolean isPrimary;
    private Instant createdAt;
    private Instant updatedAt;
}
