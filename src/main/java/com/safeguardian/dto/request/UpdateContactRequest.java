package com.safeguardian.dto.request;

import lombok.Data;

@Data
public class UpdateContactRequest {

    private String fullName;
    private String relationship;
    private String phone;
    private String email;
    private Integer priority;
    private Boolean isPrimary;
}
