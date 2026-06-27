package com.safeguardian.service;

import com.safeguardian.dto.request.AccidentAlertRequest;
import com.safeguardian.dto.request.SimulationAlertRequest;
import com.safeguardian.dto.request.SosAlertRequest;
import com.safeguardian.exception.ResourceNotFoundException;
import com.safeguardian.model.EmergencyAlert;
import com.safeguardian.model.EmergencyContact;
import com.safeguardian.model.User;
import com.safeguardian.repository.EmergencyAlertRepository;
import com.safeguardian.repository.EmergencyContactRepository;
import com.safeguardian.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmergencyAlertService {

    private final EmergencyAlertRepository alertRepository;
    private final UserRepository userRepository;
    private final EmergencyContactRepository contactRepository;
    private final EmailService emailService;

    // SOS Alert

    public EmergencyAlert sendSosAlert(SosAlertRequest req) throws Exception {
        User user = getUser(req.getUserId());
        List<EmergencyContact> contacts = contactRepository.findByUserId(req.getUserId());

        double lat = req.getLatitude() != null ? req.getLatitude() : 0.0;
        double lng = req.getLongitude() != null ? req.getLongitude() : 0.0;
        String mapUrl = buildMapUrl(lat, lng);
        String message = buildSosMessage(user.getFullName(), mapUrl);
        String now = now();

        EmergencyAlert alert = EmergencyAlert.builder()
                .userId(req.getUserId())
                .type("SOS")
                .status("PENDING")
                .latitude(lat)
                .longitude(lng)
                .mapUrl(mapUrl)
                .message(message)
                .triggerSource("MANUAL_SOS")
                .sentEmail(false)
                .createdAt(now)
                .updatedAt(now)
                .build();

        alert = alertRepository.save(alert);

        // Gửi email
        boolean emailSent = trySendEmail(user, contacts, lat, lng, null, "MANUAL_SOS");
        String sentAt = emailSent ? now() : null;

        alert = alertRepository.updateStatus(
                alert.getId(),
                emailSent ? "SENT" : "FAILED",
                sentAt,
                emailSent,
                now()
        );

        log.info("SOS alert {} - trạng thái: {}", alert.getId(), alert.getStatus());
        return alert;
    }

    // Accident Alert

    public EmergencyAlert sendAccidentAlert(AccidentAlertRequest req) throws Exception {
        User user = getUser(req.getUserId());
        List<EmergencyContact> contacts = contactRepository.findByUserId(req.getUserId());

        double lat = req.getLatitude() != null ? req.getLatitude() : 0.0;
        double lng = req.getLongitude() != null ? req.getLongitude() : 0.0;
        String mapUrl = buildMapUrl(lat, lng);
        String message = buildAccidentMessage(user.getFullName(), mapUrl);
        String now = now();

        EmergencyAlert alert = EmergencyAlert.builder()
                .userId(req.getUserId())
                .eventId(req.getEventId())
                .type("ACCIDENT")
                .status("PENDING")
                .latitude(lat)
                .longitude(lng)
                .mapUrl(mapUrl)
                .message(message)
                .triggerSource("AUTO_DETECTED")
                .sentEmail(false)
                .createdAt(now)
                .updatedAt(now)
                .build();

        alert = alertRepository.save(alert);

        boolean emailSent = trySendEmail(user, contacts, lat, lng, req.getAcceleration(), "AUTO_DETECTED");
        String sentAt = emailSent ? now() : null;

        alert = alertRepository.updateStatus(
                alert.getId(),
                emailSent ? "SENT" : "FAILED",
                sentAt,
                emailSent,
                now()
        );

        log.info("Accident alert {} - trạng thái: {}", alert.getId(), alert.getStatus());
        return alert;
    }

    //  Simulation Alert

    public EmergencyAlert sendSimulationAlert(SimulationAlertRequest req) throws Exception {
        User user = getUser(req.getUserId());
        List<EmergencyContact> contacts = contactRepository.findByUserId(req.getUserId());

        double lat = req.getLatitude() != null ? req.getLatitude() : 0.0;
        double lng = req.getLongitude() != null ? req.getLongitude() : 0.0;
        String mapUrl = buildMapUrl(lat, lng);
        String message = buildAccidentMessage(user.getFullName(), mapUrl);
        String now = now();

        EmergencyAlert alert = EmergencyAlert.builder()
                .userId(req.getUserId())
                .type("ACCIDENT")
                .status("PENDING")
                .latitude(lat)
                .longitude(lng)
                .mapUrl(mapUrl)
                .message(message)
                .triggerSource("SIMULATION")
                .sentEmail(false)
                .createdAt(now)
                .updatedAt(now)
                .build();

        alert = alertRepository.save(alert);

        boolean emailSent = trySendEmail(user, contacts, lat, lng, null, "SIMULATION");
        String sentAt = emailSent ? now() : null;

        alert = alertRepository.updateStatus(
                alert.getId(),
                emailSent ? "SENT" : "FAILED",
                sentAt,
                emailSent,
                now()
        );

        log.info("Simulation alert {} - trạng thái: {}", alert.getId(), alert.getStatus());
        return alert;
    }

    //  Get Alerts

    public List<EmergencyAlert> getAlertsByUserId(String userId) throws Exception {
        return alertRepository.findByUserId(userId);
    }

    // Cancel Alert

    public EmergencyAlert cancelAlert(String alertId, String reason) throws Exception {
        alertRepository.findById(alertId)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy cảnh báo: " + alertId));
        EmergencyAlert cancelled = alertRepository.cancel(alertId, now());
        log.info("Đã hủy alert {} - lý do: {}", alertId, reason);
        return cancelled;
    }

    // Emergency Profile

    public java.util.Map<String, Object> getEmergencyProfile(String userId) throws Exception {
        User user = getUser(userId);
        EmergencyContact primary = contactRepository.findPrimaryByUserId(userId).orElse(null);

        java.util.Map<String, Object> profile = new java.util.LinkedHashMap<>();
        profile.put("fullName", user.getFullName());
        profile.put("phone", user.getPhone());
        profile.put("bloodType", user.getBloodType());
        profile.put("medicalNote", user.getMedicalNote());
        profile.put("address", user.getAddress());

        if (primary != null) {
            java.util.Map<String, Object> primaryMap = new java.util.LinkedHashMap<>();
            primaryMap.put("fullName", primary.getFullName());
            primaryMap.put("relationship", primary.getRelationship());
            primaryMap.put("phone", primary.getPhone());
            primaryMap.put("email", primary.getEmail());
            profile.put("primaryContact", primaryMap);
        } else {
            profile.put("primaryContact", null);
        }

        return profile;
    }

    // Private helpers

    private User getUser(String userId) throws Exception {
        return userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy người dùng: " + userId));
    }

    private boolean trySendEmail(User user, List<EmergencyContact> contacts,
                                  double lat, double lng, Double acceleration, String triggerSource) {
        try {
            if ("MANUAL_SOS".equals(triggerSource)) {
                emailService.sendSosAlert(user, contacts, lat, lng);
            } else {
                emailService.sendAccidentAlert(user, contacts, lat, lng, acceleration, triggerSource);
            }
            return true;
        } catch (Exception e) {
            log.error("Lỗi gửi email cho user {}: {}", user.getId(), e.getMessage());
            return false;
        }
    }

    private String buildMapUrl(double lat, double lng) {
        return String.format("https://maps.google.com/?q=%.6f,%.6f", lat, lng);
    }

    private String buildSosMessage(String fullName, String mapUrl) {
        return fullName + " cần trợ giúp khẩn cấp.\n" + mapUrl;
    }

    private String buildAccidentMessage(String fullName, String mapUrl) {
        return fullName + " có thể đang gặp tai nạn. Vui lòng kiểm tra vị trí hiện tại.\n" + mapUrl;
    }

    private String now() {
        return LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
    }
}
