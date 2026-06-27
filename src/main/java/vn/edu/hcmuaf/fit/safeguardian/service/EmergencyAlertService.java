package vn.edu.hcmuaf.fit.safeguardian.service;

import java.util.List;

import vn.edu.hcmuaf.fit.safeguardian.dto.AccidentAlertRequest;
import vn.edu.hcmuaf.fit.safeguardian.dto.CancelAlertRequest;
import vn.edu.hcmuaf.fit.safeguardian.dto.SosAlertRequest;
import vn.edu.hcmuaf.fit.safeguardian.entity.EmergencyAlert;

public interface EmergencyAlertService {
    EmergencyAlert sendSos(SosAlertRequest request);

    EmergencyAlert sendAccident(AccidentAlertRequest request);

    EmergencyAlert sendSimulation(SosAlertRequest request);

    List<EmergencyAlert> listByUser(String userId);

    EmergencyAlert cancel(String alertId, CancelAlertRequest request);
}
