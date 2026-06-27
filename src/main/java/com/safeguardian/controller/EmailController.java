package com.safeguardian.controller;

import com.safeguardian.dto.request.TestEmailRequest;
import com.safeguardian.dto.response.ApiResponse;
import com.safeguardian.service.EmailService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/emails")
@RequiredArgsConstructor
public class EmailController {

    private final EmailService emailService;

    /** POST /api/emails/test */
    @PostMapping("/test")
    public ResponseEntity<ApiResponse<Void>> testEmail(
            @Valid @RequestBody TestEmailRequest req) {
        emailService.sendTestEmail(req.getTo(), req.getSubject(), req.getContent());
        return ResponseEntity.ok(ApiResponse.success("Gửi email test thành công", null));
    }
}
