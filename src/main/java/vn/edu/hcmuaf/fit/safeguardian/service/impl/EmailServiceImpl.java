package vn.edu.hcmuaf.fit.safeguardian.service.impl;

import java.util.List;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import lombok.RequiredArgsConstructor;
import vn.edu.hcmuaf.fit.safeguardian.entity.EmergencyAlert;
import vn.edu.hcmuaf.fit.safeguardian.entity.EmergencyContact;
import vn.edu.hcmuaf.fit.safeguardian.entity.User;
import vn.edu.hcmuaf.fit.safeguardian.exception.BadRequestException;
import vn.edu.hcmuaf.fit.safeguardian.service.EmailService;

@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {
    private final JavaMailSender mailSender;

    @Override
    public void sendTestEmail(String to, String subject, String content) {
        send(to, subject, content);
    }

    @Override
    public void sendEmergencyAlert(User user, List<EmergencyContact> contacts, EmergencyAlert alert) {
        List<String> recipients = contacts.stream()
                .map(EmergencyContact::getEmail)
                .filter(StringUtils::hasText)
                .toList();
        if (recipients.isEmpty()) {
            throw new BadRequestException("No emergency contact email available");
        }

        String subject = "SafeGuardian Emergency Alert";
        String content = alert.getMessage()
                + "\n\nUser: " + user.getFullName()
                + "\nPhone: " + user.getPhone()
                + "\nLocation: " + alert.getMapUrl();

        for (String recipient : recipients) {
            send(recipient, subject, content);
        }
    }

    private void send(String to, String subject, String content) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(content);
        mailSender.send(message);
    }
}
