package com.safeguardian.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EmergencyAlert {

    private String id;
    private String userId;
    private String eventId;

    /** ACCIDENT | SOS */
    private String type;

    /** PENDING | SENT | FAILED | CANCELLED */
    private String status;

    private Double latitude;
    private Double longitude;
    private String mapUrl;
    private String message;

    /** AUTO_DETECTED | MANUAL_SOS | SIMULATION */
    private String triggerSource;

    private Boolean sentEmail;
    private String sentAt;
    private String createdAt;
    private String updatedAt;
}
