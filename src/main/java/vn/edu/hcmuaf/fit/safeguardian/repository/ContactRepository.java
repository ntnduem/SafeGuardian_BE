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
import vn.edu.hcmuaf.fit.safeguardian.entity.EmergencyContact;
import vn.edu.hcmuaf.fit.safeguardian.util.FirestoreMapper;

@Repository
@RequiredArgsConstructor
public class ContactRepository {
    private static final String COLLECTION = "emergency_contacts";

    private final Firestore firestore;

    public EmergencyContact save(EmergencyContact contact) {
        try {
            DocumentReference document = contact.getId() == null
                    ? firestore.collection(COLLECTION).document()
                    : firestore.collection(COLLECTION).document(contact.getId());
            contact.setId(document.getId());
            document.set(toMap(contact), SetOptions.merge()).get();
            return contact;
        } catch (Exception ex) {
            throw new IllegalStateException("Cannot save contact", ex);
        }
    }

    public Optional<EmergencyContact> findById(String id) {
        try {
            DocumentSnapshot snapshot = firestore.collection(COLLECTION).document(id).get().get();
            if (!snapshot.exists() || snapshot.getData() == null) {
                return Optional.empty();
            }
            return Optional.of(fromMap(snapshot.getId(), snapshot.getData()));
        } catch (Exception ex) {
            throw new IllegalStateException("Cannot find contact", ex);
        }
    }

    public List<EmergencyContact> findByUserId(String userId) {
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
                            EmergencyContact::getPriority,
                            Comparator.nullsLast(Integer::compareTo)))
                    .toList();
        } catch (Exception ex) {
            throw new IllegalStateException("Cannot list contacts", ex);
        }
    }

    public void deleteById(String id) {
        try {
            firestore.collection(COLLECTION).document(id).delete().get();
        } catch (Exception ex) {
            throw new IllegalStateException("Cannot delete contact", ex);
        }
    }

    public void unsetPrimaryContacts(String userId, String exceptId) {
        findByUserId(userId).stream()
                .filter(contact -> Boolean.TRUE.equals(contact.getIsPrimary()))
                .filter(contact -> exceptId == null || !exceptId.equals(contact.getId()))
                .forEach(contact -> {
                    contact.setIsPrimary(false);
                    contact.setUpdatedAt(Instant.now());
                    save(contact);
                });
    }

    private Map<String, Object> toMap(EmergencyContact contact) {
        Map<String, Object> data = new HashMap<>();
        data.put("id", contact.getId());
        data.put("userId", contact.getUserId());
        data.put("fullName", contact.getFullName());
        data.put("relationship", contact.getRelationship());
        data.put("phone", contact.getPhone());
        data.put("email", contact.getEmail());
        data.put("priority", contact.getPriority());
        data.put("isPrimary", contact.getIsPrimary());
        data.put("createdAt", toDate(contact.getCreatedAt()));
        data.put("updatedAt", toDate(contact.getUpdatedAt()));
        return data;
    }

    private EmergencyContact fromMap(String id, Map<String, Object> data) {
        return EmergencyContact.builder()
                .id(id)
                .userId(FirestoreMapper.string(data, "userId"))
                .fullName(FirestoreMapper.string(data, "fullName"))
                .relationship(FirestoreMapper.string(data, "relationship"))
                .phone(FirestoreMapper.string(data, "phone"))
                .email(FirestoreMapper.string(data, "email"))
                .priority(FirestoreMapper.intValue(data.get("priority")))
                .isPrimary((Boolean) data.get("isPrimary"))
                .createdAt(FirestoreMapper.instant(data.get("createdAt")))
                .updatedAt(FirestoreMapper.instant(data.get("updatedAt")))
                .build();
    }

    private Date toDate(Instant instant) {
        return instant == null ? null : Date.from(instant);
    }
}
