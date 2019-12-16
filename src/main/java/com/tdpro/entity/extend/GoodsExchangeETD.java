package com.tdpro.entity.extend;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class GoodsExchangeETD {
    private Long id;
    private Long goodsId;
    private Long voucherId;
    @ApiModelProperty(value = "需要优惠券数量")
    private Integer number;
    @ApiModelProperty(value = "优惠券名")
    private String voucherName;
    @ApiModelProperty(value = "会员拥有优惠券数量")
    private Integer userNum;
    @ApiModelProperty(value = "会员拥有优惠券数量")
    private BigDecimal faceValue;
}
