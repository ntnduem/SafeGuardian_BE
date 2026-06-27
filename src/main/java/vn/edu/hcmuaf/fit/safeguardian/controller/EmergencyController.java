package vn.edu.hcmuaf.fit.safeguardian.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import vn.edu.hcmuaf.fit.safeguardian.dto.AccidentAlertRequest;
import vn.edu.hcmuaf.fit.safeguardian.dto.CancelAlertRequest;
import vn.edu.hcmuaf.fit.safeguardian.dto.EmergencyProfileResponse;
import vn.edu.hcmuaf.fit.safeguardian.dto.SosAlertRequest;
import vn.edu.hcmuaf.fit.safeguardian.entity.EmergencyAlert;
import vn.edu.hcmuaf.fit.safeguardian.response.ApiResponse;
import vn.edu.hcmuaf.fit.safeguardian.service.EmergencyAlertService;
import vn.edu.hcmuaf.fit.safeguardian.service.EmergencyProfileService;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class EmergencyController {
    private final EmergencyAlertService emergencyAlertService;
    private final EmergencyProfileService emergencyProfileService;

    @PostMapping("/emergency-alerts/sos")
    public ResponseEntity<ApiResponse<EmergencyAlert>> sendSos(@Valid @RequestBody SosAlertRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Da gui canh bao SOS den nguoi than", emergencyAlertService.sendSos(request)));
    }

    @PostMapping("/emergency-alerts/accident")
    public ResponseEntity<ApiResponse<EmergencyAlert>> sendAccident(@Valid @RequestBody AccidentAlertRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Da gui canh bao tai nan den nguoi than", emergencyAlertService.sendAccident(request)));
    }

    @PostMapping("/emergency-alerts/simulation")
    public ResponseEntity<ApiResponse<EmergencyAlert>> sendSimulation(@Valid @RequestBody SosAlertRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Da gui canh bao gia lap", emergencyAlertService.sendSimulation(request)));
    }

    @GetMapping("/users/{userId}/emergency-alerts")
    public ApiResponse<List<EmergencyAlert>> listByUser(@PathVariable String userId) {
        return ApiResponse.success(emergencyAlertService.listByUser(userId));
    }

    @PatchMapping("/emergency-alerts/{alertId}/cancel")
    public ApiResponse<EmergencyAlert> cancel(
            @PathVariable String alertId,
            @RequestBody(required = false) CancelAlertRequest request) {
        return ApiResponse.success("Canh bao da duoc huy", emergencyAlertService.cancel(alertId, request));
    }

    @GetMapping("/users/{userId}/emergency-profile")
    public ApiResponse<EmergencyProfileResponse> emergencyProfile(@PathVariable String userId) {
        return ApiResponse.success(emergencyProfileService.getProfile(userId));
    }
}
