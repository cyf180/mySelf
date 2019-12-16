package com.tdpro.entity.extend;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class UserUPD {
    private Long id;
    @ApiModelProperty(value = "手机号")
    private String phone;
    @ApiModelProperty(value = "真实姓名")
    private String name;
    @ApiModelProperty(value = "开户行")
    private String bankName;
    @ApiModelProperty(value = "开户支行")
    private String bankBranch;
    @ApiModelProperty(value = "银行卡号")
    private String bankCard;
    @ApiModelProperty(value = "身份证")
    private String idCard;
    private String payPassword;
}
