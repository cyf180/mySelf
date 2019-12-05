package com.tdpro.common.model;

import lombok.Data;

@Data
public class LoginRequest {
    private String code;
    private String wxHead;
    private String wxName;
}
