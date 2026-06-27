package com.safeguardian.repository;

import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import com.safeguardian.model.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Repository
@RequiredArgsConstructor
public class UserRepository {

    private static final String COLLECTION = "users";

    private final Firestore db;

    public User save(User user) throws Exception {
        if (user.getId() == null || user.getId().isBlank()) {
            user.setId("user_" + UUID.randomUUID().toString().replace("-", "").substring(0, 12));
        }

        DocumentReference docRef = db.collection(COLLECTION).document(user.getId());
        docRef.set(toMap(user)).get();
        log.debug("Đã lưu user: {}", user.getId());
        return user;
    }

    public Optional<User> findById(String userId) throws Exception {
        DocumentSnapshot doc = db.collection(COLLECTION).document(userId).get().get();
        if (!doc.exists()) {
            return Optional.empty();
        }
        User user = fromSnapshot(doc);
        return Optional.of(user);
    }

    public User update(User user) throws Exception {
        DocumentReference docRef = db.collection(COLLECTION).document(user.getId());
        docRef.update(toMap(user)).get();
        return user;
    }

    public void updateStatus(String userId, String status, String updatedAt) throws Exception {
        Map<String, Object> updates = new HashMap<>();
        updates.put("status", status);
        updates.put("updatedAt", updatedAt);
        db.collection(COLLECTION).document(userId).update(updates).get();
    }

    //  Helpers

    private Map<String, Object> toMap(User user) {
        Map<String, Object> map = new HashMap<>();
        map.put("fullName", user.getFullName());
        map.put("phone", user.getPhone());
        map.put("email", user.getEmail());
        map.put("bloodType", user.getBloodType());
        map.put("dateOfBirth", user.getDateOfBirth());
        map.put("gender", user.getGender());
        map.put("medicalNote", user.getMedicalNote());
        map.put("address", user.getAddress());
        map.put("status", user.getStatus());
        map.put("createdAt", user.getCreatedAt());
        map.put("updatedAt", user.getUpdatedAt());
        return map;
    }

    private User fromSnapshot(DocumentSnapshot doc) {
        return User.builder()
                .id(doc.getId())
                .fullName(doc.getString("fullName"))
                .phone(doc.getString("phone"))
                .email(doc.getString("email"))
                .bloodType(doc.getString("bloodType"))
                .dateOfBirth(doc.getString("dateOfBirth"))
                .gender(doc.getString("gender"))
                .medicalNote(doc.getString("medicalNote"))
                .address(doc.getString("address"))
                .status(doc.getString("status"))
                .createdAt(doc.getString("createdAt"))
                .updatedAt(doc.getString("updatedAt"))
                .build();
    }
}
