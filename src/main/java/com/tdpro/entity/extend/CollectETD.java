package com.tdpro.entity.extend;

import com.tdpro.common.utils.Page;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class CollectETD extends Page {
    private Long id;
    private Long uid;
    private Long goodsId;
    private Date createTime;
    private String goodsName;
    private String hostImg;
    private BigDecimal price;
    private String specification;
}
