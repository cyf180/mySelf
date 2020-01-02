package com.tdpro.entity.extend;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class CartETD {
    private Long id;

    private Long uid;

    private Long goodsId;

    private Long orderId;

    private Long suitId;

    private BigDecimal price;

    private BigDecimal vipPrice;

    private String suitName;

    private String goodsName;

    private Integer number;

    private Integer isKnot;

    private Date createTime;

    private String hostImg;
}
