package vn.edu.hcmuaf.fit.safeguardian.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import vn.edu.hcmuaf.fit.safeguardian.dto.UserCreateRequest;
import vn.edu.hcmuaf.fit.safeguardian.dto.UserStatusRequest;
import vn.edu.hcmuaf.fit.safeguardian.dto.UserUpdateRequest;
import vn.edu.hcmuaf.fit.safeguardian.entity.User;
import vn.edu.hcmuaf.fit.safeguardian.response.ApiResponse;
import vn.edu.hcmuaf.fit.safeguardian.service.UserService;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping
    public ResponseEntity<ApiResponse<User>> create(@Valid @RequestBody UserCreateRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Tao nguoi dung thanh cong", userService.create(request)));
    }

    @GetMapping("/{userId}")
    public ApiResponse<User> getById(@PathVariable String userId) {
        return ApiResponse.success(userService.getById(userId));
    }

    @PutMapping("/{userId}")
    public ApiResponse<User> update(@PathVariable String userId, @Valid @RequestBody UserUpdateRequest request) {
        return ApiResponse.success("Cap nhat nguoi dung thanh cong", userService.update(userId, request));
    }

    @PatchMapping("/{userId}/status")
    public ApiResponse<User> updateStatus(@PathVariable String userId, @Valid @RequestBody UserStatusRequest request) {
        return ApiResponse.success("Cap nhat trang thai nguoi dung thanh cong", userService.updateStatus(userId, request));
    }
}
