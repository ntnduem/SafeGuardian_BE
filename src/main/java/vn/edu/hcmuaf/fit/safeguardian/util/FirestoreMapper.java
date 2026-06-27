package vn.edu.hcmuaf.fit.safeguardian.util;

import java.time.Instant;
import java.util.Date;
import java.util.Map;

import com.google.cloud.Timestamp;

public final class FirestoreMapper {
    private FirestoreMapper() {
    }

    public static Instant instant(Object value) {
        if (value == null) {
            return null;
        }
        if (value instanceof Timestamp timestamp) {
            return timestamp.toDate().toInstant();
        }
        if (value instanceof Date date) {
            return date.toInstant();
        }
        if (value instanceof String text) {
            return Instant.parse(text);
        }
        return null;
    }

    public static Double doubleValue(Object value) {
        if (value instanceof Number number) {
            return number.doubleValue();
        }
        return null;
    }

    public static Integer intValue(Object value) {
        if (value instanceof Number number) {
            return number.intValue();
        }
        return null;
    }

    public static String string(Map<String, Object> data, String key) {
        Object value = data.get(key);
        return value == null ? null : value.toString();
    }
}
