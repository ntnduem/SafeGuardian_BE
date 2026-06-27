package com.safeguardian.controller;

import com.safeguardian.dto.response.ApiResponse;
import com.safeguardian.model.AccidentEvent;
import com.safeguardian.model.EmergencyAlert;
import com.safeguardian.model.EmergencyContact;
import com.safeguardian.model.User;
import com.safeguardian.repository.AccidentEventRepository;
import com.safeguardian.repository.EmergencyAlertRepository;
import com.safeguardian.repository.EmergencyContactRepository;
import com.safeguardian.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Endpoint chỉ dùng để seed dữ liệu demo.
 * Gọi POST /api/dev/seed một lần duy nhất để khởi tạo toàn bộ 4 collections
 * trong Firestore với dữ liệu mẫu.
 */
@Slf4j
@RestController
@RequestMapping("/api/dev")
@RequiredArgsConstructor
public class DevController {

    private final UserRepository userRepository;
    private final EmergencyContactRepository contactRepository;
    private final AccidentEventRepository eventRepository;
    private final EmergencyAlertRepository alertRepository;

    @PostMapping("/seed")
    public ResponseEntity<ApiResponse<Map<String, Object>>> seed() throws Exception {
        String now = LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);

        //  1. users
        User user = User.builder()
                .id("user_demo_001")
                .fullName("Nguyễn Thị Ngọc Diễm")
                .phone("0379891713")
                .email("ngocdiem@example.com")
                .bloodType("O+")
                .dateOfBirth("2003-01-01")
                .gender("FEMALE")
                .medicalNote("Không có ghi chú y tế đặc biệt")
                .address("TP. Hồ Chí Minh")
                .status("ACTIVE")
                .createdAt(now)
                .updatedAt(now)
                .build();
        userRepository.save(user);
        log.info("✅ Đã tạo user: {}", user.getId());

        //  2. emergency_contacts
        EmergencyContact father = EmergencyContact.builder()
                .id("contact_demo_001")
                .userId("user_demo_001")
                .fullName("Nguyễn Văn A")
                .relationship("Father")
                .phone("0901234567")
                .email("father@example.com")
                .priority(1)
                .isPrimary(true)
                .createdAt(now)
                .updatedAt(now)
                .build();
        contactRepository.save(father);

        EmergencyContact mother = EmergencyContact.builder()
                .id("contact_demo_002")
                .userId("user_demo_001")
                .fullName("Trần Thị B")
                .relationship("Mother")
                .phone("0912345678")
                .email("mother@example.com")
                .priority(2)
                .isPrimary(false)
                .createdAt(now)
                .updatedAt(now)
                .build();
        contactRepository.save(mother);
        log.info("✅ Đã tạo 2 emergency_contacts");

        //  3. accident_events
        AccidentEvent event = AccidentEvent.builder()
                .id("event_demo_001")
                .userId("user_demo_001")
                .eventType("STRONG_IMPACT")
                .acceleration(31.5)
                .threshold(25.0)
                .latitude(10.762622)
                .longitude(106.660172)
                .isConfirmedAccident(true)
                .createdAt(now)
                .build();
        eventRepository.save(event);
        log.info("✅ Đã tạo accident_event: {}", event.getId());

        //  4. emergency_alerts
        EmergencyAlert alert = EmergencyAlert.builder()
                .id("alert_demo_001")
                .userId("user_demo_001")
                .eventId("event_demo_001")
                .type("ACCIDENT")
                .status("SENT")
                .latitude(10.762622)
                .longitude(106.660172)
                .mapUrl("https://maps.google.com/?q=10.762622,106.660172")
                .message("Nguyễn Thị Ngọc Diễm có thể đang gặp tai nạn. Vui lòng kiểm tra vị trí hiện tại.")
                .triggerSource("SIMULATION")
                .sentEmail(true)
                .sentAt(now)
                .createdAt(now)
                .updatedAt(now)
                .build();
        alertRepository.save(alert);
        log.info("✅ Đã tạo emergency_alert: {}", alert.getId());

        //  Summary
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("users",              "✅ user_demo_001 (Nguyễn Thị Ngọc Diễm)");
        result.put("emergency_contacts", "✅ contact_demo_001 (Cha) + contact_demo_002 (Mẹ)");
        result.put("accident_events",    "✅ event_demo_001 (STRONG_IMPACT 31.5 m/s²)");
        result.put("emergency_alerts",   "✅ alert_demo_001 (SIMULATION - SENT)");
        result.put("note",               "Gọi lại endpoint này sẽ ghi đè dữ liệu cũ (upsert)");

        return ResponseEntity.ok(ApiResponse.success("Khởi tạo dữ liệu demo thành công! 4 collections đã được tạo.", result));
    }
}
