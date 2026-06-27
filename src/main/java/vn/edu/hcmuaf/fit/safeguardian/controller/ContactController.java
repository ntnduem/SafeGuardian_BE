package vn.edu.hcmuaf.fit.safeguardian.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import vn.edu.hcmuaf.fit.safeguardian.dto.ContactRequest;
import vn.edu.hcmuaf.fit.safeguardian.entity.EmergencyContact;
import vn.edu.hcmuaf.fit.safeguardian.response.ApiResponse;
import vn.edu.hcmuaf.fit.safeguardian.service.ContactService;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class ContactController {
    private final ContactService contactService;

    @PostMapping("/users/{userId}/contacts")
    public ResponseEntity<ApiResponse<EmergencyContact>> create(
            @PathVariable String userId,
            @Valid @RequestBody ContactRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Them lien he khan cap thanh cong", contactService.create(userId, request)));
    }

    @GetMapping("/users/{userId}/contacts")
    public ApiResponse<List<EmergencyContact>> listByUser(@PathVariable String userId) {
        return ApiResponse.success(contactService.listByUser(userId));
    }

    @PutMapping("/contacts/{contactId}")
    public ApiResponse<EmergencyContact> update(
            @PathVariable String contactId,
            @Valid @RequestBody ContactRequest request) {
        return ApiResponse.success("Cap nhat lien he thanh cong", contactService.update(contactId, request));
    }

    @DeleteMapping("/contacts/{contactId}")
    public ApiResponse<Void> delete(@PathVariable String contactId) {
        contactService.delete(contactId);
        return ApiResponse.success("Xoa lien he thanh cong", null);
    }
}
