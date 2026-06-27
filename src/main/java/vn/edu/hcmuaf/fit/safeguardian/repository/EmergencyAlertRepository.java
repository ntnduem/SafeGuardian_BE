package vn.edu.hcmuaf.fit.safeguardian.repository;

import java.time.Instant;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.SetOptions;

import lombok.RequiredArgsConstructor;
import vn.edu.hcmuaf.fit.safeguardian.constant.AlertStatus;
import vn.edu.hcmuaf.fit.safeguardian.constant.AlertType;
import vn.edu.hcmuaf.fit.safeguardian.constant.TriggerSource;
import vn.edu.hcmuaf.fit.safeguardian.entity.EmergencyAlert;
import vn.edu.hcmuaf.fit.safeguardian.util.FirestoreMapper;

@Repository
@RequiredArgsConstructor
public class EmergencyAlertRepository {
    private static final String COLLECTION = "emergency_alerts";

    private final Firestore firestore;

    public EmergencyAlert save(EmergencyAlert alert) {
        try {
            DocumentReference document = alert.getId() == null
                    ? firestore.collection(COLLECTION).document()
                    : firestore.collection(COLLECTION).document(alert.getId());
            alert.setId(document.getId());
            document.set(toMap(alert), SetOptions.merge()).get();
            return alert;
        } catch (Exception ex) {
            throw new IllegalStateException("Cannot save emergency alert", ex);
        }
    }

    public Optional<EmergencyAlert> findById(String id) {
        try {
            DocumentSnapshot snapshot = firestore.collection(COLLECTION).document(id).get().get();
            if (!snapshot.exists() || snapshot.getData() == null) {
                return Optional.empty();
            }
            return Optional.of(fromMap(snapshot.getId(), snapshot.getData()));
        } catch (Exception ex) {
            throw new IllegalStateException("Cannot find emergency alert", ex);
        }
    }

    public List<EmergencyAlert> findByUserId(String userId) {
        try {
            return firestore.collection(COLLECTION)
                    .whereEqualTo("userId", userId)
                    .get()
                    .get()
                    .getDocuments()
                    .stream()
                    .filter(snapshot -> snapshot.getData() != null)
                    .map(snapshot -> fromMap(snapshot.getId(), snapshot.getData()))
                    .sorted(Comparator.comparing(
                            EmergencyAlert::getCreatedAt,
                            Comparator.nullsLast(Comparator.reverseOrder())))
                    .toList();
        } catch (Exception ex) {
            throw new IllegalStateException("Cannot list emergency alerts", ex);
        }
    }

    private Map<String, Object> toMap(EmergencyAlert alert) {
        Map<String, Object> data = new HashMap<>();
        data.put("id", alert.getId());
        data.put("userId", alert.getUserId());
        data.put("eventId", alert.getEventId());
        data.put("type", alert.getType() == null ? null : alert.getType().name());
        data.put("status", alert.getStatus() == null ? null : alert.getStatus().name());
        data.put("latitude", alert.getLatitude());
        data.put("longitude", alert.getLongitude());
        data.put("mapUrl", alert.getMapUrl());
        data.put("message", alert.getMessage());
        data.put("triggerSource", alert.getTriggerSource() == null ? null : alert.getTriggerSource().name());
        data.put("sentEmail", alert.getSentEmail());
        data.put("sentAt", toDate(alert.getSentAt()));
        data.put("cancelReason", alert.getCancelReason());
        data.put("createdAt", toDate(alert.getCreatedAt()));
        data.put("updatedAt", toDate(alert.getUpdatedAt()));
        return data;
    }

    private EmergencyAlert fromMap(String id, Map<String, Object> data) {
        return EmergencyAlert.builder()
                .id(id)
                .userId(FirestoreMapper.string(data, "userId"))
                .eventId(FirestoreMapper.string(data, "eventId"))
                .type(enumValue(AlertType.class, FirestoreMapper.string(data, "type")))
                .status(enumValue(AlertStatus.class, FirestoreMapper.string(data, "status")))
                .latitude(FirestoreMapper.doubleValue(data.get("latitude")))
                .longitude(FirestoreMapper.doubleValue(data.get("longitude")))
                .mapUrl(FirestoreMapper.string(data, "mapUrl"))
                .message(FirestoreMapper.string(data, "message"))
                .triggerSource(enumValue(TriggerSource.class, FirestoreMapper.string(data, "triggerSource")))
                .sentEmail((Boolean) data.get("sentEmail"))
                .sentAt(FirestoreMapper.instant(data.get("sentAt")))
                .cancelReason(FirestoreMapper.string(data, "cancelReason"))
                .createdAt(FirestoreMapper.instant(data.get("createdAt")))
                .updatedAt(FirestoreMapper.instant(data.get("updatedAt")))
                .build();
    }

    private Date toDate(Instant instant) {
        return instant == null ? null : Date.from(instant);
    }

    private <E extends Enum<E>> E enumValue(Class<E> type, String value) {
        return value == null ? null : Enum.valueOf(type, value);
    }
}
