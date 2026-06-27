package vn.edu.hcmuaf.fit.safeguardian.service.impl;

import java.util.Comparator;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import vn.edu.hcmuaf.fit.safeguardian.dto.EmergencyProfileResponse;
import vn.edu.hcmuaf.fit.safeguardian.entity.EmergencyContact;
import vn.edu.hcmuaf.fit.safeguardian.entity.User;
import vn.edu.hcmuaf.fit.safeguardian.repository.ContactRepository;
import vn.edu.hcmuaf.fit.safeguardian.service.EmergencyProfileService;
import vn.edu.hcmuaf.fit.safeguardian.service.UserService;

@Service
@RequiredArgsConstructor
public class EmergencyProfileServiceImpl implements EmergencyProfileService {
    private final UserService userService;
    private final ContactRepository contactRepository;

    @Override
    public EmergencyProfileResponse getProfile(String userId) {
        User user = userService.getById(userId);
        EmergencyContact primaryContact = contactRepository.findByUserId(userId).stream()
                .sorted(Comparator.comparing(
                        EmergencyContact::getPriority,
                        Comparator.nullsLast(Integer::compareTo)))
                .filter(contact -> Boolean.TRUE.equals(contact.getIsPrimary()))
                .findFirst()
                .orElseGet(() -> contactRepository.findByUserId(userId).stream().findFirst().orElse(null));

        EmergencyProfileResponse.PrimaryContact contactResponse = primaryContact == null
                ? null
                : EmergencyProfileResponse.PrimaryContact.builder()
                        .fullName(primaryContact.getFullName())
                        .relationship(primaryContact.getRelationship())
                        .phone(primaryContact.getPhone())
                        .email(primaryContact.getEmail())
                        .build();

        return EmergencyProfileResponse.builder()
                .fullName(user.getFullName())
                .phone(user.getPhone())
                .bloodType(user.getBloodType())
                .medicalNote(user.getMedicalNote())
                .address(user.getAddress())
                .primaryContact(contactResponse)
                .build();
    }
}
