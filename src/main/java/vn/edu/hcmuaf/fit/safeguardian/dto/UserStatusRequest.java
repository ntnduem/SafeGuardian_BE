package vn.edu.hcmuaf.fit.safeguardian.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import vn.edu.hcmuaf.fit.safeguardian.constant.UserStatus;

@Data
public class UserStatusRequest {
    @NotNull
    private UserStatus status;
}
