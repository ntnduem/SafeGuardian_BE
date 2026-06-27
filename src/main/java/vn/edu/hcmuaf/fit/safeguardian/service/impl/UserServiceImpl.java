package vn.edu.hcmuaf.fit.safeguardian.service.impl;

import java.time.Instant;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import vn.edu.hcmuaf.fit.safeguardian.constant.UserStatus;
import vn.edu.hcmuaf.fit.safeguardian.dto.UserCreateRequest;
import vn.edu.hcmuaf.fit.safeguardian.dto.UserStatusRequest;
import vn.edu.hcmuaf.fit.safeguardian.dto.UserUpdateRequest;
import vn.edu.hcmuaf.fit.safeguardian.entity.User;
import vn.edu.hcmuaf.fit.safeguardian.exception.ResourceNotFoundException;
import vn.edu.hcmuaf.fit.safeguardian.repository.UserRepository;
import vn.edu.hcmuaf.fit.safeguardian.service.UserService;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    @Override
    public User create(UserCreateRequest request) {
        Instant now = Instant.now();
        User user = User.builder()
                .fullName(request.getFullName())
                .phone(request.getPhone())
                .email(request.getEmail())
                .bloodType(request.getBloodType())
                .dateOfBirth(request.getDateOfBirth())
                .gender(request.getGender())
                .medicalNote(request.getMedicalNote())
                .address(request.getAddress())
                .status(UserStatus.ACTIVE)
                .createdAt(now)
                .updatedAt(now)
                .build();
        return userRepository.save(user);
    }

    @Override
    public User getById(String userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found: " + userId));
    }

    @Override
    public User update(String userId, UserUpdateRequest request) {
        User user = getById(userId);
        if (request.getFullName() != null) {
            user.setFullName(request.getFullName());
        }
        if (request.getPhone() != null) {
            user.setPhone(request.getPhone());
        }
        if (request.getEmail() != null) {
            user.setEmail(request.getEmail());
        }
        if (request.getBloodType() != null) {
            user.setBloodType(request.getBloodType());
        }
        if (request.getDateOfBirth() != null) {
            user.setDateOfBirth(request.getDateOfBirth());
        }
        if (request.getGender() != null) {
            user.setGender(request.getGender());
        }
        if (request.getMedicalNote() != null) {
            user.setMedicalNote(request.getMedicalNote());
        }
        if (request.getAddress() != null) {
            user.setAddress(request.getAddress());
        }
        user.setUpdatedAt(Instant.now());
        return userRepository.save(user);
    }

    @Override
    public User updateStatus(String userId, UserStatusRequest request) {
        User user = getById(userId);
        user.setStatus(request.getStatus());
        user.setUpdatedAt(Instant.now());
        return userRepository.save(user);
    }
}
