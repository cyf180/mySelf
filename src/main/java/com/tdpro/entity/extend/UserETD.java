package com.tdpro.entity.extend;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class UserETD {
    private Long id;
    @ApiModelProperty(value = "手机号")
    private String phone;
    @ApiModelProperty(value = "余额")
    private BigDecimal balance;
    @ApiModelProperty(value = "积分")
    private BigDecimal integral;
    @ApiModelProperty(value = "团队人数")
    private Integer teamPeopleNum;
    @ApiModelProperty(value = "团队单品数")
    private Integer teamOneNum;
    @ApiModelProperty(value = "团队套装数")
    private Integer teamSuitNum;
    @ApiModelProperty(value = "级别比例(需要乘100)")
    private BigDecimal rate;
    @ApiModelProperty(value = "是否是会员(0:不是 1:是)")
    private Integer isUser;
    @ApiModelProperty(value = "收藏数量")
    private Integer collectNum;
    @ApiModelProperty(value = "优惠券数量")
    private Integer voucherNum;
    @ApiModelProperty(value = "待支付数量")
    private Integer waitingPayOrderNum;
    @ApiModelProperty(value = "待发货数量")
    private Integer waitingSendOrderNum;
    @ApiModelProperty(value = "待收货数量")
    private Integer waitingTakeOrderNum;
    private Integer suitLevelNum;
    private Integer agent;
}
