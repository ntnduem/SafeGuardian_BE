package vn.edu.hcmuaf.fit.safeguardian.repository;

import java.time.Instant;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.SetOptions;

import lombok.RequiredArgsConstructor;
import vn.edu.hcmuaf.fit.safeguardian.constant.Gender;
import vn.edu.hcmuaf.fit.safeguardian.constant.UserStatus;
import vn.edu.hcmuaf.fit.safeguardian.entity.User;
import vn.edu.hcmuaf.fit.safeguardian.util.FirestoreMapper;

@Repository
@RequiredArgsConstructor
public class UserRepository {
    private static final String COLLECTION = "users";

    private final Firestore firestore;

    public User save(User user) {
        try {
            DocumentReference document = user.getId() == null
                    ? firestore.collection(COLLECTION).document()
                    : firestore.collection(COLLECTION).document(user.getId());
            user.setId(document.getId());
            document.set(toMap(user), SetOptions.merge()).get();
            return user;
        } catch (Exception ex) {
            throw new IllegalStateException("Cannot save user", ex);
        }
    }

    public Optional<User> findById(String id) {
        try {
            DocumentSnapshot snapshot = firestore.collection(COLLECTION).document(id).get().get();
            if (!snapshot.exists() || snapshot.getData() == null) {
                return Optional.empty();
            }
            return Optional.of(fromMap(snapshot.getId(), snapshot.getData()));
        } catch (Exception ex) {
            throw new IllegalStateException("Cannot find user", ex);
        }
    }

    public boolean existsById(String id) {
        return findById(id).isPresent();
    }

    private Map<String, Object> toMap(User user) {
        Map<String, Object> data = new HashMap<>();
        data.put("id", user.getId());
        data.put("fullName", user.getFullName());
        data.put("phone", user.getPhone());
        data.put("email", user.getEmail());
        data.put("bloodType", user.getBloodType());
        data.put("dateOfBirth", user.getDateOfBirth());
        data.put("gender", user.getGender() == null ? null : user.getGender().name());
        data.put("medicalNote", user.getMedicalNote());
        data.put("address", user.getAddress());
        data.put("status", user.getStatus() == null ? null : user.getStatus().name());
        data.put("createdAt", toDate(user.getCreatedAt()));
        data.put("updatedAt", toDate(user.getUpdatedAt()));
        return data;
    }

    private User fromMap(String id, Map<String, Object> data) {
        return User.builder()
                .id(id)
                .fullName(FirestoreMapper.string(data, "fullName"))
                .phone(FirestoreMapper.string(data, "phone"))
                .email(FirestoreMapper.string(data, "email"))
                .bloodType(FirestoreMapper.string(data, "bloodType"))
                .dateOfBirth(FirestoreMapper.string(data, "dateOfBirth"))
                .gender(enumValue(Gender.class, FirestoreMapper.string(data, "gender")))
                .medicalNote(FirestoreMapper.string(data, "medicalNote"))
                .address(FirestoreMapper.string(data, "address"))
                .status(enumValue(UserStatus.class, FirestoreMapper.string(data, "status")))
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
