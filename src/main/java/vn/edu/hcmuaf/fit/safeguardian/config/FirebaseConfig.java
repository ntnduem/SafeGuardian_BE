package vn.edu.hcmuaf.fit.safeguardian.config;

import java.io.FileInputStream;
import java.io.IOException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.util.StringUtils;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.firestore.Firestore;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.cloud.FirestoreClient;

@Configuration
@Profile("!test")
public class FirebaseConfig {

    @Bean
    public Firestore firestore(@Value("${firebase.credentials.path:}") String credentialsPath) throws IOException {
        if (!StringUtils.hasText(credentialsPath)) {
            throw new IllegalStateException("Missing firebase.credentials.path or FIREBASE_CREDENTIALS_PATH");
        }

        if (FirebaseApp.getApps().isEmpty()) {
            try (FileInputStream serviceAccount = new FileInputStream(credentialsPath)) {
                FirebaseOptions options = FirebaseOptions.builder()
                        .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                        .build();
                FirebaseApp.initializeApp(options);
            }
        }

        return FirestoreClient.getFirestore();
    }
}
