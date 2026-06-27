package com.safeguardian.repository;

import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.QuerySnapshot;
import com.safeguardian.model.EmergencyContact;
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
public class EmergencyContactRepository {

    private static final String COLLECTION = "emergency_contacts";

    private final Firestore db;

    public EmergencyContact save(EmergencyContact contact) throws Exception {
        if (contact.getId() == null || contact.getId().isBlank()) {
            contact.setId("contact_" + UUID.randomUUID().toString().replace("-", "").substring(0, 12));
        }

        DocumentReference docRef = db.collection(COLLECTION).document(contact.getId());
        docRef.set(toMap(contact)).get();
        log.debug("Đã lưu contact: {}", contact.getId());
        return contact;
    }

    public Optional<EmergencyContact> findById(String contactId) throws Exception {
        DocumentSnapshot doc = db.collection(COLLECTION).document(contactId).get().get();
        if (!doc.exists()) {
            return Optional.empty();
        }
        return Optional.of(fromSnapshot(doc));
    }

    public List<EmergencyContact> findByUserId(String userId) throws Exception {
        QuerySnapshot querySnapshot = db.collection(COLLECTION)
                .whereEqualTo("userId", userId)
                .orderBy("priority")
                .get()
                .get();

        List<EmergencyContact> contacts = new ArrayList<>();
        for (DocumentSnapshot doc : querySnapshot.getDocuments()) {
            contacts.add(fromSnapshot(doc));
        }
        return contacts;
    }

    public Optional<EmergencyContact> findPrimaryByUserId(String userId) throws Exception {
        QuerySnapshot querySnapshot = db.collection(COLLECTION)
                .whereEqualTo("userId", userId)
                .whereEqualTo("isPrimary", true)
                .limit(1)
                .get()
                .get();

        if (querySnapshot.isEmpty()) {
            // Nếu không có primary, trả về người có priority = 1
            QuerySnapshot fallback = db.collection(COLLECTION)
                    .whereEqualTo("userId", userId)
                    .orderBy("priority")
                    .limit(1)
                    .get()
                    .get();
            if (fallback.isEmpty()) return Optional.empty();
            return Optional.of(fromSnapshot(fallback.getDocuments().get(0)));
        }
        return Optional.of(fromSnapshot(querySnapshot.getDocuments().get(0)));
    }

    public EmergencyContact update(EmergencyContact contact) throws Exception {
        db.collection(COLLECTION).document(contact.getId()).update(toMap(contact)).get();
        return contact;
    }

    public void delete(String contactId) throws Exception {
        db.collection(COLLECTION).document(contactId).delete().get();
        log.debug("Đã xóa contact: {}", contactId);
    }

    //Helpers

    private Map<String, Object> toMap(EmergencyContact c) {
        Map<String, Object> map = new HashMap<>();
        map.put("userId", c.getUserId());
        map.put("fullName", c.getFullName());
        map.put("relationship", c.getRelationship());
        map.put("phone", c.getPhone());
        map.put("email", c.getEmail());
        map.put("priority", c.getPriority());
        map.put("isPrimary", c.getIsPrimary());
        map.put("createdAt", c.getCreatedAt());
        map.put("updatedAt", c.getUpdatedAt());
        return map;
    }

    private EmergencyContact fromSnapshot(DocumentSnapshot doc) {
        return EmergencyContact.builder()
                .id(doc.getId())
                .userId(doc.getString("userId"))
                .fullName(doc.getString("fullName"))
                .relationship(doc.getString("relationship"))
                .phone(doc.getString("phone"))
                .email(doc.getString("email"))
                .priority(doc.getLong("priority") != null ? doc.getLong("priority").intValue() : null)
                .isPrimary(doc.getBoolean("isPrimary"))
                .createdAt(doc.getString("createdAt"))
                .updatedAt(doc.getString("updatedAt"))
                .build();
    }
}
