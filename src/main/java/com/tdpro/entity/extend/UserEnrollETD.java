package com.tdpro.entity.extend;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class UserEnrollETD {
    private String phone;
    private String payPassword;
    private Long strawUid;
    private String code;
    private Long loginId;
}
