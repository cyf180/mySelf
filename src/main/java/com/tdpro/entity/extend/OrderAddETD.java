package com.tdpro.entity.extend;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
public class OrderAddETD {
    private Long uid;
    @ApiModelProperty(value = "商品id")
    private Long goodsId;
    List<CartAddETD> cartAddList;
}
