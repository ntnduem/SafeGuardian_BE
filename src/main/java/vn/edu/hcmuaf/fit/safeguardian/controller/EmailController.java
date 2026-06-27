package vn.edu.hcmuaf.fit.safeguardian.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import vn.edu.hcmuaf.fit.safeguardian.dto.TestEmailRequest;
import vn.edu.hcmuaf.fit.safeguardian.response.ApiResponse;
import vn.edu.hcmuaf.fit.safeguardian.service.EmailService;

@RestController
@RequestMapping("/api/emails")
@RequiredArgsConstructor
public class EmailController {
    private final EmailService emailService;

    @PostMapping("/test")
    public ApiResponse<Void> sendTest(@Valid @RequestBody TestEmailRequest request) {
        emailService.sendTestEmail(request.getTo(), request.getSubject(), request.getContent());
        return ApiResponse.success("Gui email test thanh cong", null);
    }
}
