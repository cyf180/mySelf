package com.tdpro.entity.extend;

import com.tdpro.common.utils.QueryModel;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class VoucherPageETD extends QueryModel {
    private Long id;

    private String voucherName;

    private BigDecimal faceValue;

    private BigDecimal price;

    private Integer isSill;

    private BigDecimal mainSill;

    private Integer issueNum;

    private Integer useNum;

    private Integer timeType;

    private Date startTime;

    private Date endTime;

    private Integer state;

    private Integer isDel;

    private Date createTime;

    private String useExplain;

    private Integer circulation;
}
