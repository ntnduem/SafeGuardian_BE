package com.safeguardian.service;

import com.safeguardian.dto.request.CreateContactRequest;
import com.safeguardian.dto.request.UpdateContactRequest;
import com.safeguardian.exception.ResourceNotFoundException;
import com.safeguardian.model.EmergencyContact;
import com.safeguardian.repository.EmergencyContactRepository;
import com.safeguardian.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmergencyContactService {

    private final EmergencyContactRepository contactRepository;
    private final UserRepository userRepository;

    public EmergencyContact createContact(String userId, CreateContactRequest req) throws Exception {
        // Kiểm tra user tồn tại
        userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy người dùng: " + userId));

        String now = now();
        EmergencyContact contact = EmergencyContact.builder()
                .userId(userId)
                .fullName(req.getFullName())
                .relationship(req.getRelationship())
                .phone(req.getPhone())
                .email(req.getEmail())
                .priority(req.getPriority() != null ? req.getPriority() : 99)
                .isPrimary(req.getIsPrimary() != null ? req.getIsPrimary() : false)
                .createdAt(now)
                .updatedAt(now)
                .build();

        return contactRepository.save(contact);
    }

    public List<EmergencyContact> getContactsByUserId(String userId) throws Exception {
        return contactRepository.findByUserId(userId);
    }

    public EmergencyContact updateContact(String contactId, UpdateContactRequest req) throws Exception {
        EmergencyContact existing = contactRepository.findById(contactId)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy người liên hệ: " + contactId));

        if (req.getFullName() != null) existing.setFullName(req.getFullName());
        if (req.getRelationship() != null) existing.setRelationship(req.getRelationship());
        if (req.getPhone() != null) existing.setPhone(req.getPhone());
        if (req.getEmail() != null) existing.setEmail(req.getEmail());
        if (req.getPriority() != null) existing.setPriority(req.getPriority());
        if (req.getIsPrimary() != null) existing.setIsPrimary(req.getIsPrimary());
        existing.setUpdatedAt(now());

        return contactRepository.update(existing);
    }

    public void deleteContact(String contactId) throws Exception {
        contactRepository.findById(contactId)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy người liên hệ: " + contactId));
        contactRepository.delete(contactId);
    }

    private String now() {
        return LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
    }
}
