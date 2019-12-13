package com.tdpro.entity.extend;

import com.tdpro.common.utils.Page;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class WithdrawETD extends Page {
    private Long id;
    @ApiModelProperty(value = "审核状态 -1：失败，0：审核中，1：成功")
    private Integer status;
    private Long uid;
    @ApiModelProperty(value = "提现金额")
    private BigDecimal amount;
    @ApiModelProperty(value = "实际到账金额")
    private BigDecimal arrivalAmount;
    @ApiModelProperty(value = "手续费")
    private BigDecimal poundage;
    @ApiModelProperty(value = "余额")
    private BigDecimal balance;
    @ApiModelProperty(value = "手续费利率")
    private BigDecimal rate;
    @ApiModelProperty(value = "申请时间")
    private Date applyTime;
    @ApiModelProperty(value = "审核时间")
    private Date auditTime;
    @ApiModelProperty(value = "姓名")
    private String name;
    @ApiModelProperty(value = "身份证")
    private String idCard;
    @ApiModelProperty(value = "银行卡号")
    private String bankCard;
    @ApiModelProperty(value = "银行名")
    private String bankName;
    @ApiModelProperty(value = "开户行")
    private String bankBranch;
    @ApiModelProperty(value = "备注")
    private String note;
}
