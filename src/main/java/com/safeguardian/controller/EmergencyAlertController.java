package com.safeguardian.controller;

import com.safeguardian.dto.request.AccidentAlertRequest;
import com.safeguardian.dto.request.CancelAlertRequest;
import com.safeguardian.dto.request.SimulationAlertRequest;
import com.safeguardian.dto.request.SosAlertRequest;
import com.safeguardian.dto.response.ApiResponse;
import com.safeguardian.model.EmergencyAlert;
import com.safeguardian.service.EmergencyAlertService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class EmergencyAlertController {

    private final EmergencyAlertService alertService;

    /** POST /api/emergency-alerts/sos */
    @PostMapping("/emergency-alerts/sos")
    public ResponseEntity<ApiResponse<EmergencyAlert>> sendSos(
            @Valid @RequestBody SosAlertRequest req) throws Exception {
        EmergencyAlert alert = alertService.sendSosAlert(req);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Đã gửi cảnh báo SOS đến người thân", alert));
    }

    /** POST /api/emergency-alerts/accident */
    @PostMapping("/emergency-alerts/accident")
    public ResponseEntity<ApiResponse<EmergencyAlert>> sendAccident(
            @Valid @RequestBody AccidentAlertRequest req) throws Exception {
        EmergencyAlert alert = alertService.sendAccidentAlert(req);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Đã gửi cảnh báo tai nạn đến người thân", alert));
    }

    /** POST /api/emergency-alerts/simulation */
    @PostMapping("/emergency-alerts/simulation")
    public ResponseEntity<ApiResponse<EmergencyAlert>> sendSimulation(
            @Valid @RequestBody SimulationAlertRequest req) throws Exception {
        EmergencyAlert alert = alertService.sendSimulationAlert(req);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Đã gửi cảnh báo giả lập", alert));
    }

    /** GET /api/users/{userId}/emergency-alerts */
    @GetMapping("/users/{userId}/emergency-alerts")
    public ResponseEntity<ApiResponse<List<EmergencyAlert>>> getAlerts(
            @PathVariable String userId) throws Exception {
        List<EmergencyAlert> alerts = alertService.getAlertsByUserId(userId);
        return ResponseEntity.ok(ApiResponse.success(alerts));
    }

    /** PATCH /api/emergency-alerts/{alertId}/cancel */
    @PatchMapping("/emergency-alerts/{alertId}/cancel")
    public ResponseEntity<ApiResponse<EmergencyAlert>> cancelAlert(
            @PathVariable String alertId,
            @RequestBody(required = false) CancelAlertRequest req) throws Exception {
        String reason = req != null ? req.getReason() : "Người dùng xác nhận an toàn";
        EmergencyAlert alert = alertService.cancelAlert(alertId, reason);
        return ResponseEntity.ok(ApiResponse.success("Cảnh báo đã được hủy", alert));
    }

    /** GET /api/users/{userId}/emergency-profile */
    @GetMapping("/users/{userId}/emergency-profile")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getEmergencyProfile(
            @PathVariable String userId) throws Exception {
        Map<String, Object> profile = alertService.getEmergencyProfile(userId);
        return ResponseEntity.ok(ApiResponse.success(profile));
    }
}
