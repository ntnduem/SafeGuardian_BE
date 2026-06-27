package vn.edu.hcmuaf.fit.safeguardian.service.impl;

import java.time.Instant;
import java.util.List;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import vn.edu.hcmuaf.fit.safeguardian.dto.AccidentConfirmRequest;
import vn.edu.hcmuaf.fit.safeguardian.dto.AccidentEventCreateRequest;
import vn.edu.hcmuaf.fit.safeguardian.entity.AccidentEvent;
import vn.edu.hcmuaf.fit.safeguardian.exception.ResourceNotFoundException;
import vn.edu.hcmuaf.fit.safeguardian.repository.AccidentEventRepository;
import vn.edu.hcmuaf.fit.safeguardian.service.AccidentEventService;
import vn.edu.hcmuaf.fit.safeguardian.service.UserService;

@Service
@RequiredArgsConstructor
public class AccidentEventServiceImpl implements AccidentEventService {
    private final AccidentEventRepository accidentEventRepository;
    private final UserService userService;

    @Override
    public AccidentEvent create(AccidentEventCreateRequest request) {
        userService.getById(request.getUserId());
        AccidentEvent event = AccidentEvent.builder()
                .userId(request.getUserId())
                .eventType(request.getEventType())
                .acceleration(request.getAcceleration())
                .threshold(request.getThreshold())
                .latitude(request.getLatitude())
                .longitude(request.getLongitude())
                .isConfirmedAccident(Boolean.TRUE.equals(request.getIsConfirmedAccident()))
                .createdAt(Instant.now())
                .build();
        return accidentEventRepository.save(event);
    }

    @Override
    public AccidentEvent confirm(String eventId, AccidentConfirmRequest request) {
        AccidentEvent event = accidentEventRepository.findById(eventId)
                .orElseThrow(() -> new ResourceNotFoundException("Accident event not found: " + eventId));
        event.setIsConfirmedAccident(request.getIsConfirmedAccident());
        return accidentEventRepository.save(event);
    }

    @Override
    public List<AccidentEvent> listByUser(String userId) {
        userService.getById(userId);
        return accidentEventRepository.findByUserId(userId);
    }
}
