package vn.edu.hcmuaf.fit.safeguardian.service;

import java.util.List;

import vn.edu.hcmuaf.fit.safeguardian.entity.EmergencyAlert;
import vn.edu.hcmuaf.fit.safeguardian.entity.EmergencyContact;
import vn.edu.hcmuaf.fit.safeguardian.entity.User;

public interface EmailService {
    void sendTestEmail(String to, String subject, String content);

    void sendEmergencyAlert(User user, List<EmergencyContact> contacts, EmergencyAlert alert);
}
