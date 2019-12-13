package com.tdpro.entity.extend;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class UserWithdrawETD {
    @ApiModelProperty(value = "真实姓名")
    private String name;
    @ApiModelProperty(value = "余额")
    private BigDecimal balance;
    @ApiModelProperty(value = "开户行")
    private String bankName;
    @ApiModelProperty(value = "开户支行")
    private String bankBranch;
    @ApiModelProperty(value = "银行卡号")
    private String bankCard;
    @ApiModelProperty(value = "身份证")
    private String idCard;
    @ApiModelProperty(value = "提现手续费")
    private BigDecimal rate;
}
