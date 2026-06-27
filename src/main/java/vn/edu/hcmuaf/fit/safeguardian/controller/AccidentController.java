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
import vn.edu.hcmuaf.fit.safeguardian.dto.AccidentConfirmRequest;
import vn.edu.hcmuaf.fit.safeguardian.dto.AccidentEventCreateRequest;
import vn.edu.hcmuaf.fit.safeguardian.entity.AccidentEvent;
import vn.edu.hcmuaf.fit.safeguardian.response.ApiResponse;
import vn.edu.hcmuaf.fit.safeguardian.service.AccidentEventService;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class AccidentController {
    private final AccidentEventService accidentEventService;

    @PostMapping("/accident-events")
    public ResponseEntity<ApiResponse<AccidentEvent>> create(@Valid @RequestBody AccidentEventCreateRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Ghi nhan su kien cam bien thanh cong", accidentEventService.create(request)));
    }

    @PatchMapping("/accident-events/{eventId}/confirm")
    public ApiResponse<AccidentEvent> confirm(
            @PathVariable String eventId,
            @Valid @RequestBody AccidentConfirmRequest request) {
        return ApiResponse.success("Cap nhat xac nhan tai nan thanh cong", accidentEventService.confirm(eventId, request));
    }

    @GetMapping("/users/{userId}/accident-events")
    public ApiResponse<List<AccidentEvent>> listByUser(@PathVariable String userId) {
        return ApiResponse.success(accidentEventService.listByUser(userId));
    }
}
