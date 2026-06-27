package vn.edu.hcmuaf.fit.safeguardian;

import static org.mockito.Mockito.mock;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.test.context.ActiveProfiles;

import com.google.cloud.firestore.Firestore;

@SpringBootTest
@ActiveProfiles("test")
class SafeguardianApiApplicationTests {

	@Test
	void contextLoads() {
	}

	@TestConfiguration
	static class TestConfig {
		@Bean
		Firestore firestore() {
			return mock(Firestore.class);
		}

		@Bean
		JavaMailSender javaMailSender() {
			return mock(JavaMailSender.class);
		}
	}
}
