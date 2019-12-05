package com.tdpro.common.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class LoginResult {
    @ApiModelProperty("会员是否已绑定手机:1是,0:否")
    int isBindPhone;
    @ApiModelProperty("用户token")
    String token;
    @ApiModelProperty("客服电话")
    String serviceTel;
    @ApiModelProperty("用户电话")
    String phone;
    String key;
}
