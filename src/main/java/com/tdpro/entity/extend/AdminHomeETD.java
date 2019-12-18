package com.tdpro.entity.extend;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class AdminHomeETD {
    private Integer orderNum;
    private BigDecimal todaySales;
    private BigDecimal yesterdaySales;
    private BigDecimal sevenSales;
    private Integer stayPayNum;
    private Integer stayHairNum;
    private Integer hairNum;
    private Integer achieveNum;
    private Integer todayUserNum;
    private Integer yesterdayUserNum;
    private Integer monthUserNum;
    private Integer allUserNum;
}
