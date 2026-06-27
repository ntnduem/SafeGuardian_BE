package vn.edu.hcmuaf.fit.safeguardian.entity;

import java.time.Instant;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import vn.edu.hcmuaf.fit.safeguardian.constant.AlertStatus;
import vn.edu.hcmuaf.fit.safeguardian.constant.AlertType;
import vn.edu.hcmuaf.fit.safeguardian.constant.TriggerSource;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EmergencyAlert {
    private String id;
    private String userId;
    private String eventId;
    private AlertType type;
    private AlertStatus status;
    private Double latitude;
    private Double longitude;
    private String mapUrl;
    private String message;
    private TriggerSource triggerSource;
    private Boolean sentEmail;
    private Instant sentAt;
    private String cancelReason;
    private Instant createdAt;
    private Instant updatedAt;
}
