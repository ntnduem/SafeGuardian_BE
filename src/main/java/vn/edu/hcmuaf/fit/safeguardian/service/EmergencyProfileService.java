package vn.edu.hcmuaf.fit.safeguardian.service;

import vn.edu.hcmuaf.fit.safeguardian.dto.EmergencyProfileResponse;

public interface EmergencyProfileService {
    EmergencyProfileResponse getProfile(String userId);
}
