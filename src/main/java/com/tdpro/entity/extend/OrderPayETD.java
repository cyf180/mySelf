package com.tdpro.entity.extend;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class OrderPayETD {
    private Long orderId;
    private Long uid;
    private Integer payType;
    private Long voucherId;
    @ApiModelProperty(value = "用户备注")
    private String userNote;
    @ApiModelProperty(value = "收货地址id")
    private Long siteId;
    private String payPassword;
}
