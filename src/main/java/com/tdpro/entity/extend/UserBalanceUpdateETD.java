package com.tdpro.entity.extend;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class UserBalanceUpdateETD {
    private Long id;
    private Integer isUser;
    private BigDecimal balance;
    private BigDecimal oldBalance;
    private BigDecimal integral;
    private BigDecimal oldIntegral;
    private BigDecimal totalIntegral;
    private BigDecimal oldTotalIntegral;
    private BigDecimal knotAmount;
    private BigDecimal oldKnotAmount;
    private BigDecimal itemBuyAmount;
    private BigDecimal itemLeftAmount;
    private BigDecimal oldItemLeftAmount;
    private BigDecimal topUpBalance;
    private BigDecimal oldTopUpBalance;
    private Integer teamOneNum;
    private Integer teamSuitNum;
    private Integer suitLevelNum;
    private Long knotId;
}
