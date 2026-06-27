package vn.edu.hcmuaf.fit.safeguardian.service.impl;

import java.time.Instant;
import java.util.List;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import vn.edu.hcmuaf.fit.safeguardian.dto.ContactRequest;
import vn.edu.hcmuaf.fit.safeguardian.entity.EmergencyContact;
import vn.edu.hcmuaf.fit.safeguardian.exception.ResourceNotFoundException;
import vn.edu.hcmuaf.fit.safeguardian.repository.ContactRepository;
import vn.edu.hcmuaf.fit.safeguardian.service.ContactService;
import vn.edu.hcmuaf.fit.safeguardian.service.UserService;

@Service
@RequiredArgsConstructor
public class ContactServiceImpl implements ContactService {
    private final ContactRepository contactRepository;
    private final UserService userService;

    @Override
    public EmergencyContact create(String userId, ContactRequest request) {
        userService.getById(userId);
        if (Boolean.TRUE.equals(request.getIsPrimary())) {
            contactRepository.unsetPrimaryContacts(userId, null);
        }
        Instant now = Instant.now();
        EmergencyContact contact = EmergencyContact.builder()
                .userId(userId)
                .fullName(request.getFullName())
                .relationship(request.getRelationship())
                .phone(request.getPhone())
                .email(request.getEmail())
                .priority(request.getPriority())
                .isPrimary(Boolean.TRUE.equals(request.getIsPrimary()))
                .createdAt(now)
                .updatedAt(now)
                .build();
        return contactRepository.save(contact);
    }

    @Override
    public List<EmergencyContact> listByUser(String userId) {
        userService.getById(userId);
        return contactRepository.findByUserId(userId);
    }

    @Override
    public EmergencyContact update(String contactId, ContactRequest request) {
        EmergencyContact contact = contactRepository.findById(contactId)
                .orElseThrow(() -> new ResourceNotFoundException("Contact not found: " + contactId));
        if (Boolean.TRUE.equals(request.getIsPrimary())) {
            contactRepository.unsetPrimaryContacts(contact.getUserId(), contactId);
        }
        contact.setFullName(request.getFullName());
        contact.setRelationship(request.getRelationship());
        contact.setPhone(request.getPhone());
        contact.setEmail(request.getEmail());
        contact.setPriority(request.getPriority());
        contact.setIsPrimary(Boolean.TRUE.equals(request.getIsPrimary()));
        contact.setUpdatedAt(Instant.now());
        return contactRepository.save(contact);
    }

    @Override
    public void delete(String contactId) {
        contactRepository.findById(contactId)
                .orElseThrow(() -> new ResourceNotFoundException("Contact not found: " + contactId));
        contactRepository.deleteById(contactId);
    }
}
