package vn.edu.hcmuaf.fit.safeguardian.util;

public final class MapUrlUtil {
    private MapUrlUtil() {
    }

    public static String googleMapsUrl(Double latitude, Double longitude) {
        return "https://maps.google.com/?q=" + latitude + "," + longitude;
    }
}
