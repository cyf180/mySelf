package com.tdpro.entity.extend;

import com.tdpro.common.utils.Page;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
@Data
public class UserKnotETD extends Page {
    private Long id;

    private Long uid;

    private Long payUid;

    private Long orderId;

    private Long monthId;
    @ApiModelProperty(value = "结算金额")
    private BigDecimal knotPrice;
    @ApiModelProperty(value = "订单支付金额")
    private BigDecimal payPrice;
    @ApiModelProperty(value = "结算类型(0:套装购买积分结算 1:月提成积分结算 2:积分转换 3:单品购买结算)")
    private Integer knotType;
    @ApiModelProperty(value = "时间")
    private Date createTime;
    @ApiModelProperty(value = "收益人账号")
    private String phone;
}
