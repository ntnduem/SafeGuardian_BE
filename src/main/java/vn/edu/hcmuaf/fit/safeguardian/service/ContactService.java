package vn.edu.hcmuaf.fit.safeguardian.service;

import java.util.List;

import vn.edu.hcmuaf.fit.safeguardian.dto.ContactRequest;
import vn.edu.hcmuaf.fit.safeguardian.entity.EmergencyContact;

public interface ContactService {
    EmergencyContact create(String userId, ContactRequest request);

    List<EmergencyContact> listByUser(String userId);

    EmergencyContact update(String contactId, ContactRequest request);

    void delete(String contactId);
}
