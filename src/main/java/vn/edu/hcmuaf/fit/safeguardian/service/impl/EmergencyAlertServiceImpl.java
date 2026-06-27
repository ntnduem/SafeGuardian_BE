package vn.edu.hcmuaf.fit.safeguardian.service.impl;

import java.time.Instant;
import java.util.List;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import vn.edu.hcmuaf.fit.safeguardian.constant.AlertStatus;
import vn.edu.hcmuaf.fit.safeguardian.constant.AlertType;
import vn.edu.hcmuaf.fit.safeguardian.constant.TriggerSource;
import vn.edu.hcmuaf.fit.safeguardian.dto.AccidentAlertRequest;
import vn.edu.hcmuaf.fit.safeguardian.dto.CancelAlertRequest;
import vn.edu.hcmuaf.fit.safeguardian.dto.SosAlertRequest;
import vn.edu.hcmuaf.fit.safeguardian.entity.EmergencyAlert;
import vn.edu.hcmuaf.fit.safeguardian.entity.EmergencyContact;
import vn.edu.hcmuaf.fit.safeguardian.entity.User;
import vn.edu.hcmuaf.fit.safeguardian.exception.ResourceNotFoundException;
import vn.edu.hcmuaf.fit.safeguardian.repository.AccidentEventRepository;
import vn.edu.hcmuaf.fit.safeguardian.repository.ContactRepository;
import vn.edu.hcmuaf.fit.safeguardian.repository.EmergencyAlertRepository;
import vn.edu.hcmuaf.fit.safeguardian.service.EmailService;
import vn.edu.hcmuaf.fit.safeguardian.service.EmergencyAlertService;
import vn.edu.hcmuaf.fit.safeguardian.service.UserService;
import vn.edu.hcmuaf.fit.safeguardian.util.MapUrlUtil;

@Service
@RequiredArgsConstructor
public class EmergencyAlertServiceImpl implements EmergencyAlertService {
    private final EmergencyAlertRepository emergencyAlertRepository;
    private final AccidentEventRepository accidentEventRepository;
    private final ContactRepository contactRepository;
    private final UserService userService;
    private final EmailService emailService;

    @Override
    public EmergencyAlert sendSos(SosAlertRequest request) {
        return createAndSend(
                request.getUserId(),
                null,
                AlertType.SOS,
                TriggerSource.MANUAL_SOS,
                request.getLatitude(),
                request.getLongitude(),
                null);
    }

    @Override
    public EmergencyAlert sendAccident(AccidentAlertRequest request) {
        accidentEventRepository.findById(request.getEventId())
                .orElseThrow(() -> new ResourceNotFoundException("Accident event not found: " + request.getEventId()));
        return createAndSend(
                request.getUserId(),
                request.getEventId(),
                AlertType.ACCIDENT,
                TriggerSource.AUTO_DETECTED,
                request.getLatitude(),
                request.getLongitude(),
                request.getAcceleration());
    }

    @Override
    public EmergencyAlert sendSimulation(SosAlertRequest request) {
        return createAndSend(
                request.getUserId(),
                null,
                AlertType.ACCIDENT,
                TriggerSource.SIMULATION,
                request.getLatitude(),
                request.getLongitude(),
                null);
    }

    @Override
    public List<EmergencyAlert> listByUser(String userId) {
        userService.getById(userId);
        return emergencyAlertRepository.findByUserId(userId);
    }

    @Override
    public EmergencyAlert cancel(String alertId, CancelAlertRequest request) {
        EmergencyAlert alert = emergencyAlertRepository.findById(alertId)
                .orElseThrow(() -> new ResourceNotFoundException("Emergency alert not found: " + alertId));
        alert.setStatus(AlertStatus.CANCELLED);
        alert.setCancelReason(request == null ? null : request.getReason());
        alert.setUpdatedAt(Instant.now());
        return emergencyAlertRepository.save(alert);
    }

    private EmergencyAlert createAndSend(
            String userId,
            String eventId,
            AlertType type,
            TriggerSource triggerSource,
            Double latitude,
            Double longitude,
            Double acceleration) {
        User user = userService.getById(userId);
        List<EmergencyContact> contacts = contactRepository.findByUserId(userId);
        Instant now = Instant.now();
        EmergencyAlert alert = EmergencyAlert.builder()
                .userId(userId)
                .eventId(eventId)
                .type(type)
                .status(AlertStatus.PENDING)
                .latitude(latitude)
                .longitude(longitude)
                .mapUrl(MapUrlUtil.googleMapsUrl(latitude, longitude))
                .message(buildMessage(user, type, triggerSource, acceleration))
                .triggerSource(triggerSource)
                .sentEmail(false)
                .createdAt(now)
                .updatedAt(now)
                .build();
        alert = emergencyAlertRepository.save(alert);

        try {
            emailService.sendEmergencyAlert(user, contacts, alert);
            alert.setStatus(AlertStatus.SENT);
            alert.setSentEmail(true);
            alert.setSentAt(Instant.now());
        } catch (Exception ex) {
            alert.setStatus(AlertStatus.FAILED);
            alert.setSentEmail(false);
        }
        alert.setUpdatedAt(Instant.now());
        return emergencyAlertRepository.save(alert);
    }

    private String buildMessage(User user, AlertType type, TriggerSource triggerSource, Double acceleration) {
        if (triggerSource == TriggerSource.MANUAL_SOS) {
            return user.getFullName() + " da bam nut SOS. Vui long kiem tra vi tri hien tai.";
        }
        if (triggerSource == TriggerSource.SIMULATION) {
            return user.getFullName() + " dang gui canh bao gia lap SafeGuardian.";
        }
        String accelerationText = acceleration == null ? "" : " Gia toc ghi nhan: " + acceleration + ".";
        return user.getFullName() + " co the dang gap tai nan. Vui long kiem tra vi tri hien tai." + accelerationText;
    }
}
