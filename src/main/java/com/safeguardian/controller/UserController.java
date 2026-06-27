package com.safeguardian.controller;

import com.safeguardian.dto.request.CreateUserRequest;
import com.safeguardian.dto.request.UpdateUserRequest;
import com.safeguardian.dto.request.UpdateUserStatusRequest;
import com.safeguardian.dto.response.ApiResponse;
import com.safeguardian.model.User;
import com.safeguardian.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    /** POST /api/users */
    @PostMapping
    public ResponseEntity<ApiResponse<User>> createUser(@Valid @RequestBody CreateUserRequest req) throws Exception {
        User user = userService.createUser(req);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Tạo người dùng thành công", user));
    }

    /** GET /api/users/{userId} */
    @GetMapping("/{userId}")
    public ResponseEntity<ApiResponse<User>> getUser(@PathVariable String userId) throws Exception {
        User user = userService.getUserById(userId);
        return ResponseEntity.ok(ApiResponse.success(user));
    }

    /** PUT /api/users/{userId} */
    @PutMapping("/{userId}")
    public ResponseEntity<ApiResponse<User>> updateUser(
            @PathVariable String userId,
            @RequestBody UpdateUserRequest req) throws Exception {
        User user = userService.updateUser(userId, req);
        return ResponseEntity.ok(ApiResponse.success("Cập nhật người dùng thành công", user));
    }

    /** PATCH /api/users/{userId}/status */
    @PatchMapping("/{userId}/status")
    public ResponseEntity<ApiResponse<Void>> updateStatus(
            @PathVariable String userId,
            @Valid @RequestBody UpdateUserStatusRequest req) throws Exception {
        userService.updateStatus(userId, req.getStatus());
        return ResponseEntity.ok(ApiResponse.success("Cập nhật trạng thái thành công", null));
    }
}
