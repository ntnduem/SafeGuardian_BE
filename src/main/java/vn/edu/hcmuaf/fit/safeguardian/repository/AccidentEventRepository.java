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
import vn.edu.hcmuaf.fit.safeguardian.constant.AccidentEventType;
import vn.edu.hcmuaf.fit.safeguardian.entity.AccidentEvent;
import vn.edu.hcmuaf.fit.safeguardian.util.FirestoreMapper;

@Repository
@RequiredArgsConstructor
public class AccidentEventRepository {
    private static final String COLLECTION = "accident_events";

    private final Firestore firestore;

    public AccidentEvent save(AccidentEvent event) {
        try {
            DocumentReference document = event.getId() == null
                    ? firestore.collection(COLLECTION).document()
                    : firestore.collection(COLLECTION).document(event.getId());
            event.setId(document.getId());
            document.set(toMap(event), SetOptions.merge()).get();
            return event;
        } catch (Exception ex) {
            throw new IllegalStateException("Cannot save accident event", ex);
        }
    }

    public Optional<AccidentEvent> findById(String id) {
        try {
            DocumentSnapshot snapshot = firestore.collection(COLLECTION).document(id).get().get();
            if (!snapshot.exists() || snapshot.getData() == null) {
                return Optional.empty();
            }
            return Optional.of(fromMap(snapshot.getId(), snapshot.getData()));
        } catch (Exception ex) {
            throw new IllegalStateException("Cannot find accident event", ex);
        }
    }

    public List<AccidentEvent> findByUserId(String userId) {
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
                            AccidentEvent::getCreatedAt,
                            Comparator.nullsLast(Comparator.reverseOrder())))
                    .toList();
        } catch (Exception ex) {
            throw new IllegalStateException("Cannot list accident events", ex);
        }
    }

    private Map<String, Object> toMap(AccidentEvent event) {
        Map<String, Object> data = new HashMap<>();
        data.put("id", event.getId());
        data.put("userId", event.getUserId());
        data.put("eventType", event.getEventType() == null ? null : event.getEventType().name());
        data.put("acceleration", event.getAcceleration());
        data.put("threshold", event.getThreshold());
        data.put("latitude", event.getLatitude());
        data.put("longitude", event.getLongitude());
        data.put("isConfirmedAccident", event.getIsConfirmedAccident());
        data.put("createdAt", toDate(event.getCreatedAt()));
        return data;
    }

    private AccidentEvent fromMap(String id, Map<String, Object> data) {
        return AccidentEvent.builder()
                .id(id)
                .userId(FirestoreMapper.string(data, "userId"))
                .eventType(enumValue(AccidentEventType.class, FirestoreMapper.string(data, "eventType")))
                .acceleration(FirestoreMapper.doubleValue(data.get("acceleration")))
                .threshold(FirestoreMapper.doubleValue(data.get("threshold")))
                .latitude(FirestoreMapper.doubleValue(data.get("latitude")))
                .longitude(FirestoreMapper.doubleValue(data.get("longitude")))
                .isConfirmedAccident((Boolean) data.get("isConfirmedAccident"))
                .createdAt(FirestoreMapper.instant(data.get("createdAt")))
                .build();
    }

    private Date toDate(Instant instant) {
        return instant == null ? null : Date.from(instant);
    }

    private <E extends Enum<E>> E enumValue(Class<E> type, String value) {
        return value == null ? null : Enum.valueOf(type, value);
    }
}
