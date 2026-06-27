package vn.edu.hcmuaf.fit.safeguardian.entity;

import java.time.Instant;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import vn.edu.hcmuaf.fit.safeguardian.constant.AccidentEventType;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AccidentEvent {
    private String id;
    private String userId;
    private AccidentEventType eventType;
    private Double acceleration;
    private Double threshold;
    private Double latitude;
    private Double longitude;
    private Boolean isConfirmedAccident;
    private Instant createdAt;
}
