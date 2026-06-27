package com.safeguardian.controller;

import com.safeguardian.dto.request.CreateContactRequest;
import com.safeguardian.dto.request.UpdateContactRequest;
import com.safeguardian.dto.response.ApiResponse;
import com.safeguardian.model.EmergencyContact;
import com.safeguardian.service.EmergencyContactService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class EmergencyContactController {

    private final EmergencyContactService contactService;

    /** POST /api/users/{userId}/contacts */
    @PostMapping("/api/users/{userId}/contacts")
    public ResponseEntity<ApiResponse<EmergencyContact>> createContact(
            @PathVariable String userId,
            @Valid @RequestBody CreateContactRequest req) throws Exception {
        EmergencyContact contact = contactService.createContact(userId, req);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Thêm liên hệ khẩn cấp thành công", contact));
    }

    /** GET /api/users/{userId}/contacts */
    @GetMapping("/api/users/{userId}/contacts")
    public ResponseEntity<ApiResponse<List<EmergencyContact>>> getContacts(
            @PathVariable String userId) throws Exception {
        List<EmergencyContact> contacts = contactService.getContactsByUserId(userId);
        return ResponseEntity.ok(ApiResponse.success(contacts));
    }

    /** PUT /api/contacts/{contactId} */
    @PutMapping("/api/contacts/{contactId}")
    public ResponseEntity<ApiResponse<EmergencyContact>> updateContact(
            @PathVariable String contactId,
            @RequestBody UpdateContactRequest req) throws Exception {
        EmergencyContact contact = contactService.updateContact(contactId, req);
        return ResponseEntity.ok(ApiResponse.success("Cập nhật liên hệ thành công", contact));
    }

    /** DELETE /api/contacts/{contactId} */
    @DeleteMapping("/api/contacts/{contactId}")
    public ResponseEntity<ApiResponse<Void>> deleteContact(
            @PathVariable String contactId) throws Exception {
        contactService.deleteContact(contactId);
        return ResponseEntity.ok(ApiResponse.success("Xóa liên hệ thành công", null));
    }
}
