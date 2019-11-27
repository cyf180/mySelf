package com.tdpro.entity.extend;

import lombok.Data;

@Data
public class OrderPayETD {
    private Long orderId;
    private Long uid;
    private Integer payType;
}
