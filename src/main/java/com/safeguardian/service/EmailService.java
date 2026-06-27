package com.safeguardian.service;

import com.safeguardian.model.EmergencyContact;
import com.safeguardian.model.User;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;

    @Value("${spring.mail.from}")
    private String fromAddress;

    // ── Public methods ────────────────────────────────────────────────────────

    public void sendSosAlert(User user, List<EmergencyContact> contacts, double lat, double lng) {
        String mapUrl = buildMapUrl(lat, lng);
        String subject = "[SafeGuardian] 🆘 CẦN GIÚP ĐỠ KHẨN CẤP - " + user.getFullName();
        String html = buildSosEmailHtml(user, mapUrl, lat, lng);
        sendToContacts(contacts, subject, html);
    }

    public void sendAccidentAlert(User user, List<EmergencyContact> contacts,
                                   double lat, double lng, Double acceleration, String triggerSource) {
        String mapUrl = buildMapUrl(lat, lng);
        String subject = "[SafeGuardian] ⚠️ CẢNH BÁO TAI NẠN - " + user.getFullName();
        String html = buildAccidentEmailHtml(user, mapUrl, lat, lng, acceleration, triggerSource);
        sendToContacts(contacts, subject, html);
    }

    public void sendTestEmail(String to, String subject, String content) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            helper.setFrom(fromAddress);
            helper.setTo(to);
            helper.setSubject(subject != null ? subject : "SafeGuardian Test Email");
            helper.setText(content != null ? content : "Email kiểm tra từ SafeGuardian.", false);
            mailSender.send(message);
            log.info("Gửi email test thành công đến: {}", to);
        } catch (Exception e) {
            log.error("Lỗi khi gửi email test đến {}: {}", to, e.getMessage());
            throw new RuntimeException("Gửi email thất bại: " + e.getMessage(), e);
        }
    }

    // ── Private helpers ───────────────────────────────────────────────────────

    private void sendToContacts(List<EmergencyContact> contacts, String subject, String html) {
        if (contacts == null || contacts.isEmpty()) {
            log.warn("Không có người liên hệ khẩn cấp nào để gửi email.");
            return;
        }

        for (EmergencyContact contact : contacts) {
            if (contact.getEmail() == null || contact.getEmail().isBlank()) {
                log.warn("Người liên hệ {} không có email, bỏ qua.", contact.getFullName());
                continue;
            }
            try {
                MimeMessage message = mailSender.createMimeMessage();
                MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
                helper.setFrom(fromAddress);
                helper.setTo(contact.getEmail());
                helper.setSubject(subject);
                helper.setText(html, true);
                mailSender.send(message);
                log.info("Gửi email cảnh báo thành công đến: {} ({})", contact.getFullName(), contact.getEmail());
            } catch (Exception e) {
                log.error("Lỗi khi gửi email đến {}: {}", contact.getEmail(), e.getMessage());
            }
        }
    }

    private String buildMapUrl(double lat, double lng) {
        return String.format("https://maps.google.com/?q=%.6f,%.6f", lat, lng);
    }

    private String buildSosEmailHtml(User user, String mapUrl, double lat, double lng) {
        return """
                <!DOCTYPE html>
                <html lang="vi">
                <head><meta charset="UTF-8"><meta name="viewport" content="width=device-width, initial-scale=1.0"></head>
                <body style="margin:0;padding:0;font-family:Arial,sans-serif;background:#f5f5f5;">
                  <table width="100%%" cellpadding="0" cellspacing="0" style="background:#f5f5f5;padding:20px 0;">
                    <tr><td align="center">
                      <table width="600" cellpadding="0" cellspacing="0" style="background:#fff;border-radius:12px;overflow:hidden;box-shadow:0 4px 12px rgba(0,0,0,0.1);">
                        <!-- Header -->
                        <tr><td style="background:linear-gradient(135deg,#e53e3e,#c53030);padding:32px;text-align:center;">
                          <h1 style="color:#fff;margin:0;font-size:28px;">🆘 CẦN GIÚP ĐỠ KHẨN CẤP</h1>
                          <p style="color:rgba(255,255,255,0.9);margin:8px 0 0;font-size:16px;">SafeGuardian Alert</p>
                        </td></tr>
                        <!-- Body -->
                        <tr><td style="padding:32px;">
                          <p style="font-size:18px;color:#2d3748;margin-bottom:24px;">
                            <strong>%s</strong> đang cần được giúp đỡ ngay lập tức!
                          </p>
                          <!-- Medical Info -->
                          <table width="100%%" cellpadding="12" cellspacing="0" style="background:#fff5f5;border:1px solid #feb2b2;border-radius:8px;margin-bottom:24px;">
                            <tr><td><strong style="color:#c53030;">Thông tin y tế khẩn cấp</strong></td></tr>
                            <tr><td>🩸 <strong>Nhóm máu:</strong> %s</td></tr>
                            <tr><td>📋 <strong>Ghi chú bệnh lý:</strong> %s</td></tr>
                            <tr><td>📞 <strong>SĐT:</strong> %s</td></tr>
                          </table>
                          <!-- Location -->
                          <table width="100%%" cellpadding="12" cellspacing="0" style="background:#f0fff4;border:1px solid #9ae6b4;border-radius:8px;margin-bottom:24px;">
                            <tr><td><strong style="color:#276749;">📍 Vị trí hiện tại</strong></td></tr>
                            <tr><td>Tọa độ: %.6f, %.6f</td></tr>
                          </table>
                          <!-- Map Button -->
                          <div style="text-align:center;margin:24px 0;">
                            <a href="%s" style="background:#e53e3e;color:#fff;padding:14px 32px;border-radius:8px;text-decoration:none;font-size:16px;font-weight:bold;display:inline-block;">
                              🗺️ XEM VỊ TRÍ TRÊN BẢN ĐỒ
                            </a>
                          </div>
                          <p style="color:#718096;font-size:14px;text-align:center;">Vui lòng liên hệ ngay hoặc gọi 113/114/115 nếu cần thiết.</p>
                        </td></tr>
                        <!-- Footer -->
                        <tr><td style="background:#2d3748;padding:16px;text-align:center;">
                          <p style="color:#a0aec0;margin:0;font-size:12px;">SafeGuardian • Ứng dụng hỗ trợ an toàn cá nhân</p>
                        </td></tr>
                      </table>
                    </td></tr>
                  </table>
                </body>
                </html>
                """.formatted(
                user.getFullName(),
                notNull(user.getBloodType()),
                notNull(user.getMedicalNote()),
                notNull(user.getPhone()),
                lat, lng,
                mapUrl
        );
    }

    private String buildAccidentEmailHtml(User user, String mapUrl, double lat, double lng,
                                           Double acceleration, String triggerSource) {
        String triggerLabel = switch (triggerSource != null ? triggerSource : "") {
            case "SIMULATION" -> "Giả lập (Demo)";
            case "MANUAL_SOS" -> "SOS thủ công";
            default -> "Phát hiện tự động";
        };

        String accelText = acceleration != null
                ? String.format("%.1f m/s²", acceleration)
                : "N/A";

        return """
                <!DOCTYPE html>
                <html lang="vi">
                <head><meta charset="UTF-8"><meta name="viewport" content="width=device-width, initial-scale=1.0"></head>
                <body style="margin:0;padding:0;font-family:Arial,sans-serif;background:#f5f5f5;">
                  <table width="100%%" cellpadding="0" cellspacing="0" style="background:#f5f5f5;padding:20px 0;">
                    <tr><td align="center">
                      <table width="600" cellpadding="0" cellspacing="0" style="background:#fff;border-radius:12px;overflow:hidden;box-shadow:0 4px 12px rgba(0,0,0,0.1);">
                        <!-- Header -->
                        <tr><td style="background:linear-gradient(135deg,#dd6b20,#c05621);padding:32px;text-align:center;">
                          <h1 style="color:#fff;margin:0;font-size:28px;">⚠️ CẢNH BÁO TAI NẠN</h1>
                          <p style="color:rgba(255,255,255,0.9);margin:8px 0 0;font-size:16px;">SafeGuardian Alert</p>
                        </td></tr>
                        <!-- Body -->
                        <tr><td style="padding:32px;">
                          <p style="font-size:18px;color:#2d3748;margin-bottom:24px;">
                            <strong>%s</strong> có thể đang gặp tai nạn. Vui lòng kiểm tra ngay!
                          </p>
                          <!-- Event Info -->
                          <table width="100%%" cellpadding="12" cellspacing="0" style="background:#fffaf0;border:1px solid #fbd38d;border-radius:8px;margin-bottom:24px;">
                            <tr><td><strong style="color:#c05621;">Chi tiết sự kiện</strong></td></tr>
                            <tr><td>⚡ <strong>Loại:</strong> %s</td></tr>
                            <tr><td>📊 <strong>Gia tốc phát hiện:</strong> %s</td></tr>
                          </table>
                          <!-- Medical Info -->
                          <table width="100%%" cellpadding="12" cellspacing="0" style="background:#fff5f5;border:1px solid #feb2b2;border-radius:8px;margin-bottom:24px;">
                            <tr><td><strong style="color:#c53030;">Thông tin y tế</strong></td></tr>
                            <tr><td>🩸 <strong>Nhóm máu:</strong> %s</td></tr>
                            <tr><td>📋 <strong>Ghi chú bệnh lý:</strong> %s</td></tr>
                            <tr><td>📞 <strong>SĐT:</strong> %s</td></tr>
                          </table>
                          <!-- Location -->
                          <table width="100%%" cellpadding="12" cellspacing="0" style="background:#f0fff4;border:1px solid #9ae6b4;border-radius:8px;margin-bottom:24px;">
                            <tr><td><strong style="color:#276749;">📍 Vị trí phát hiện</strong></td></tr>
                            <tr><td>Tọa độ: %.6f, %.6f</td></tr>
                          </table>
                          <!-- Map Button -->
                          <div style="text-align:center;margin:24px 0;">
                            <a href="%s" style="background:#dd6b20;color:#fff;padding:14px 32px;border-radius:8px;text-decoration:none;font-size:16px;font-weight:bold;display:inline-block;">
                              🗺️ XEM VỊ TRÍ TRÊN BẢN ĐỒ
                            </a>
                          </div>
                          <p style="color:#718096;font-size:14px;text-align:center;">Hãy liên hệ với họ ngay hoặc gọi cấp cứu 115 nếu cần.</p>
                        </td></tr>
                        <!-- Footer -->
                        <tr><td style="background:#2d3748;padding:16px;text-align:center;">
                          <p style="color:#a0aec0;margin:0;font-size:12px;">SafeGuardian • Ứng dụng hỗ trợ an toàn cá nhân</p>
                        </td></tr>
                      </table>
                    </td></tr>
                  </table>
                </body>
                </html>
                """.formatted(
                user.getFullName(),
                triggerLabel,
                accelText,
                notNull(user.getBloodType()),
                notNull(user.getMedicalNote()),
                notNull(user.getPhone()),
                lat, lng,
                mapUrl
        );
    }

    private String notNull(String value) {
        return value != null && !value.isBlank() ? value : "Không có";
    }
}
