package vn.edu.hcmuaf.fit.safeguardian.service;

import vn.edu.hcmuaf.fit.safeguardian.dto.UserCreateRequest;
import vn.edu.hcmuaf.fit.safeguardian.dto.UserStatusRequest;
import vn.edu.hcmuaf.fit.safeguardian.dto.UserUpdateRequest;
import vn.edu.hcmuaf.fit.safeguardian.entity.User;

public interface UserService {
    User create(UserCreateRequest request);

    User getById(String userId);

    User update(String userId, UserUpdateRequest request);

    User updateStatus(String userId, UserStatusRequest request);
}
