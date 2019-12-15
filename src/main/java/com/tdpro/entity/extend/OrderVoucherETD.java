package com.tdpro.entity.extend;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class OrderVoucherETD {
    private Long id;
    private String voucherName;
    private Integer num;
    private BigDecimal price;
}
