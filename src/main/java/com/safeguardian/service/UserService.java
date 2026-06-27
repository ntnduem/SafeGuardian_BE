package com.safeguardian.service;

import com.safeguardian.dto.request.CreateUserRequest;
import com.safeguardian.dto.request.UpdateUserRequest;
import com.safeguardian.exception.ResourceNotFoundException;
import com.safeguardian.model.User;
import com.safeguardian.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public User createUser(CreateUserRequest req) throws Exception {
        String now = now();
        User user = User.builder()
                .fullName(req.getFullName())
                .phone(req.getPhone())
                .email(req.getEmail())
                .bloodType(req.getBloodType())
                .dateOfBirth(req.getDateOfBirth())
                .gender(req.getGender())
                .medicalNote(req.getMedicalNote())
                .address(req.getAddress())
                .status("ACTIVE")
                .createdAt(now)
                .updatedAt(now)
                .build();

        return userRepository.save(user);
    }

    public User getUserById(String userId) throws Exception {
        return userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy người dùng: " + userId));
    }

    public User updateUser(String userId, UpdateUserRequest req) throws Exception {
        User existing = getUserById(userId);

        if (req.getFullName() != null) existing.setFullName(req.getFullName());
        if (req.getPhone() != null) existing.setPhone(req.getPhone());
        if (req.getEmail() != null) existing.setEmail(req.getEmail());
        if (req.getBloodType() != null) existing.setBloodType(req.getBloodType());
        if (req.getDateOfBirth() != null) existing.setDateOfBirth(req.getDateOfBirth());
        if (req.getGender() != null) existing.setGender(req.getGender());
        if (req.getMedicalNote() != null) existing.setMedicalNote(req.getMedicalNote());
        if (req.getAddress() != null) existing.setAddress(req.getAddress());
        existing.setUpdatedAt(now());

        return userRepository.update(existing);
    }

    public void updateStatus(String userId, String status) throws Exception {
        getUserById(userId); // validate existence
        userRepository.updateStatus(userId, status, now());
    }

    private String now() {
        return LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
    }
}
