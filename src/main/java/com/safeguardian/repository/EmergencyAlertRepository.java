package com.safeguardian.repository;

import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.QuerySnapshot;
import com.safeguardian.model.EmergencyAlert;
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
public class EmergencyAlertRepository {

    private static final String COLLECTION = "emergency_alerts";

    private final Firestore db;

    public EmergencyAlert save(EmergencyAlert alert) throws Exception {
        if (alert.getId() == null || alert.getId().isBlank()) {
            alert.setId("alert_" + UUID.randomUUID().toString().replace("-", "").substring(0, 12));
        }

        DocumentReference docRef = db.collection(COLLECTION).document(alert.getId());
        docRef.set(toMap(alert)).get();
        log.debug("Đã lưu emergency_alert: {}", alert.getId());
        return alert;
    }

    public Optional<EmergencyAlert> findById(String alertId) throws Exception {
        DocumentSnapshot doc = db.collection(COLLECTION).document(alertId).get().get();
        if (!doc.exists()) {
            return Optional.empty();
        }
        return Optional.of(fromSnapshot(doc));
    }

    public List<EmergencyAlert> findByUserId(String userId) throws Exception {
        QuerySnapshot querySnapshot = db.collection(COLLECTION)
                .whereEqualTo("userId", userId)
                .orderBy("createdAt", com.google.cloud.firestore.Query.Direction.DESCENDING)
                .get()
                .get();

        List<EmergencyAlert> alerts = new ArrayList<>();
        for (DocumentSnapshot doc : querySnapshot.getDocuments()) {
            alerts.add(fromSnapshot(doc));
        }
        return alerts;
    }

    public EmergencyAlert updateStatus(String alertId, String status, String sentAt,
                                       Boolean sentEmail, String updatedAt) throws Exception {
        Map<String, Object> updates = new HashMap<>();
        updates.put("status", status);
        updates.put("updatedAt", updatedAt);
        if (sentAt != null) updates.put("sentAt", sentAt);
        if (sentEmail != null) updates.put("sentEmail", sentEmail);

        db.collection(COLLECTION).document(alertId).update(updates).get();

        DocumentSnapshot doc = db.collection(COLLECTION).document(alertId).get().get();
        return fromSnapshot(doc);
    }

    public EmergencyAlert cancel(String alertId, String updatedAt) throws Exception {
        Map<String, Object> updates = new HashMap<>();
        updates.put("status", "CANCELLED");
        updates.put("updatedAt", updatedAt);
        db.collection(COLLECTION).document(alertId).update(updates).get();

        DocumentSnapshot doc = db.collection(COLLECTION).document(alertId).get().get();
        return fromSnapshot(doc);
    }

    // ── Helpers ──────────────────────────────────────────────────────────────

    private Map<String, Object> toMap(EmergencyAlert a) {
        Map<String, Object> map = new HashMap<>();
        map.put("userId", a.getUserId());
        map.put("eventId", a.getEventId());
        map.put("type", a.getType());
        map.put("status", a.getStatus());
        map.put("latitude", a.getLatitude());
        map.put("longitude", a.getLongitude());
        map.put("mapUrl", a.getMapUrl());
        map.put("message", a.getMessage());
        map.put("triggerSource", a.getTriggerSource());
        map.put("sentEmail", a.getSentEmail());
        map.put("sentAt", a.getSentAt());
        map.put("createdAt", a.getCreatedAt());
        map.put("updatedAt", a.getUpdatedAt());
        return map;
    }

    private EmergencyAlert fromSnapshot(DocumentSnapshot doc) {
        return EmergencyAlert.builder()
                .id(doc.getId())
                .userId(doc.getString("userId"))
                .eventId(doc.getString("eventId"))
                .type(doc.getString("type"))
                .status(doc.getString("status"))
                .latitude(doc.getDouble("latitude"))
                .longitude(doc.getDouble("longitude"))
                .mapUrl(doc.getString("mapUrl"))
                .message(doc.getString("message"))
                .triggerSource(doc.getString("triggerSource"))
                .sentEmail(doc.getBoolean("sentEmail"))
                .sentAt(doc.getString("sentAt"))
                .createdAt(doc.getString("createdAt"))
                .updatedAt(doc.getString("updatedAt"))
                .build();
    }
}
