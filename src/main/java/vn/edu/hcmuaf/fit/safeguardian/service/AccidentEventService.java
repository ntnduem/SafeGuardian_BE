package vn.edu.hcmuaf.fit.safeguardian.service;

import java.util.List;

import vn.edu.hcmuaf.fit.safeguardian.dto.AccidentConfirmRequest;
import vn.edu.hcmuaf.fit.safeguardian.dto.AccidentEventCreateRequest;
import vn.edu.hcmuaf.fit.safeguardian.entity.AccidentEvent;

public interface AccidentEventService {
    AccidentEvent create(AccidentEventCreateRequest request);

    AccidentEvent confirm(String eventId, AccidentConfirmRequest request);

    List<AccidentEvent> listByUser(String userId);
}
