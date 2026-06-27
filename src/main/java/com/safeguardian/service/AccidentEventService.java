package com.safeguardian.service;

import com.safeguardian.dto.request.ConfirmAccidentRequest;
import com.safeguardian.dto.request.CreateAccidentEventRequest;
import com.safeguardian.exception.ResourceNotFoundException;
import com.safeguardian.model.AccidentEvent;
import com.safeguardian.repository.AccidentEventRepository;
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
public class AccidentEventService {

    private final AccidentEventRepository eventRepository;
    private final UserRepository userRepository;

    public AccidentEvent createEvent(CreateAccidentEventRequest req) throws Exception {
        userRepository.findById(req.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy người dùng: " + req.getUserId()));

        AccidentEvent event = AccidentEvent.builder()
                .userId(req.getUserId())
                .eventType(req.getEventType() != null ? req.getEventType() : "STRONG_IMPACT")
                .acceleration(req.getAcceleration())
                .threshold(req.getThreshold())
                .latitude(req.getLatitude())
                .longitude(req.getLongitude())
                .isConfirmedAccident(req.getIsConfirmedAccident() != null ? req.getIsConfirmedAccident() : false)
                .createdAt(now())
                .build();

        return eventRepository.save(event);
    }

    public AccidentEvent confirmAccident(String eventId, ConfirmAccidentRequest req) throws Exception {
        eventRepository.findById(eventId)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy sự kiện: " + eventId));
        eventRepository.confirmAccident(eventId, req.getIsConfirmedAccident(), now());
        return eventRepository.findById(eventId).get();
    }

    public List<AccidentEvent> getEventsByUserId(String userId) throws Exception {
        return eventRepository.findByUserId(userId);
    }

    private String now() {
        return LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
    }
}
