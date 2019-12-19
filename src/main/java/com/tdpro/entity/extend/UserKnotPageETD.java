package com.tdpro.entity.extend;

import com.tdpro.common.utils.QueryModel;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class UserKnotPageETD extends QueryModel {
    private Long id;
    private Long uid;
    private Long payUid;
    private Long orderId;
    private Long monthId;
    private BigDecimal knotPrice;
    private BigDecimal payPrice;
    private Integer knotType;
    private Date createTime;
    private String phone;
    private String payPhone;
}
