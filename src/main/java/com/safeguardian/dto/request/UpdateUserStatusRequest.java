package com.safeguardian.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class UpdateUserStatusRequest {

    @NotBlank(message = "Status không được để trống")
    private String status;
}
