package com.tdpro.entity.extend;

import com.tdpro.common.utils.QueryModel;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
@Data
public class UserMonthKnotPageETD extends QueryModel {
    private Long id;
    private Long uid;
    private BigDecimal knotPrice;
    private Integer year;
    private Integer month;
    private Integer newOrderNum;
    private BigDecimal newOrderPrice;
    private Date createTime;
    private BigDecimal rate;
    private String phone;
}
