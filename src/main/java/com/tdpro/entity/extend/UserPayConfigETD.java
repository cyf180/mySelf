package com.tdpro.entity.extend;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class UserPayConfigETD {
    private BigDecimal userBuy;
    private BigDecimal depositRate;
}
