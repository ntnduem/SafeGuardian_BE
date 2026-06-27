package com.safeguardian.repository;

import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.QuerySnapshot;
import com.safeguardian.model.AccidentEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Repository
@RequiredArgsConstructor
public class AccidentEventRepository {

    private static final String COLLECTION = "accident_events";

    private final Firestore db;

    public AccidentEvent save(AccidentEvent event) throws Exception {
        if (event.getId() == null || event.getId().isBlank()) {
            event.setId("event_" + UUID.randomUUID().toString().replace("-", "").substring(0, 12));
        }

        DocumentReference docRef = db.collection(COLLECTION).document(event.getId());
        docRef.set(toMap(event)).get();
        log.debug("Đã lưu accident_event: {}", event.getId());
        return event;
    }

    public Optional<AccidentEvent> findById(String eventId) throws Exception {
        DocumentSnapshot doc = db.collection(COLLECTION).document(eventId).get().get();
        if (!doc.exists()) {
            return Optional.empty();
        }
        return Optional.of(fromSnapshot(doc));
    }

    public List<AccidentEvent> findByUserId(String userId) throws Exception {
        QuerySnapshot querySnapshot = db.collection(COLLECTION)
                .whereEqualTo("userId", userId)
                .orderBy("createdAt", com.google.cloud.firestore.Query.Direction.DESCENDING)
                .get()
                .get();

        List<AccidentEvent> events = new ArrayList<>();
        for (DocumentSnapshot doc : querySnapshot.getDocuments()) {
            events.add(fromSnapshot(doc));
        }
        return events;
    }

    public void confirmAccident(String eventId, Boolean confirmed, String updatedAt) throws Exception {
        Map<String, Object> updates = new HashMap<>();
        updates.put("isConfirmedAccident", confirmed);
        db.collection(COLLECTION).document(eventId).update(updates).get();
    }

    // Helpers

    private Map<String, Object> toMap(AccidentEvent e) {
        Map<String, Object> map = new HashMap<>();
        map.put("userId", e.getUserId());
        map.put("deviceId", e.getDeviceId());
        map.put("eventType", e.getEventType());
        map.put("acceleration", e.getAcceleration());
        map.put("threshold", e.getThreshold());
        map.put("latitude", e.getLatitude());
        map.put("longitude", e.getLongitude());
        map.put("isConfirmedAccident", e.getIsConfirmedAccident());
        map.put("createdAt", e.getCreatedAt());
        return map;
    }

    private AccidentEvent fromSnapshot(DocumentSnapshot doc) {
        return AccidentEvent.builder()
                .id(doc.getId())
                .userId(doc.getString("userId"))
                .deviceId(doc.getString("deviceId"))
                .eventType(doc.getString("eventType"))
                .acceleration(doc.getDouble("acceleration"))
                .threshold(doc.getDouble("threshold"))
                .latitude(doc.getDouble("latitude"))
                .longitude(doc.getDouble("longitude"))
                .isConfirmedAccident(doc.getBoolean("isConfirmedAccident"))
                .createdAt(doc.getString("createdAt"))
                .build();
    }
}
