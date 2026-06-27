package com.safeguardian.controller;

import com.safeguardian.dto.request.ConfirmAccidentRequest;
import com.safeguardian.dto.request.CreateAccidentEventRequest;
import com.safeguardian.dto.response.ApiResponse;
import com.safeguardian.model.AccidentEvent;
import com.safeguardian.service.AccidentEventService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class AccidentEventController {

    private final AccidentEventService eventService;

    /** POST /api/accident-events */
    @PostMapping("/api/accident-events")
    public ResponseEntity<ApiResponse<AccidentEvent>> createEvent(
            @Valid @RequestBody CreateAccidentEventRequest req) throws Exception {
        AccidentEvent event = eventService.createEvent(req);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Ghi nhận sự kiện cảm biến thành công", event));
    }

    /** PATCH /api/accident-events/{eventId}/confirm */
    @PatchMapping("/api/accident-events/{eventId}/confirm")
    public ResponseEntity<ApiResponse<AccidentEvent>> confirmAccident(
            @PathVariable String eventId,
            @RequestBody ConfirmAccidentRequest req) throws Exception {
        AccidentEvent event = eventService.confirmAccident(eventId, req);
        return ResponseEntity.ok(ApiResponse.success("Cập nhật xác nhận tai nạn thành công", event));
    }

    /** GET /api/users/{userId}/accident-events */
    @GetMapping("/api/users/{userId}/accident-events")
    public ResponseEntity<ApiResponse<List<AccidentEvent>>> getEvents(
            @PathVariable String userId) throws Exception {
        List<AccidentEvent> events = eventService.getEventsByUserId(userId);
        return ResponseEntity.ok(ApiResponse.success(events));
    }
}
