package com.tdpro.entity.extend;

import com.tdpro.common.utils.QueryModel;
import com.tdpro.entity.PGoodsImg;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Data
public class GoodsPageETD extends QueryModel {
    private Long id;
    private Long classId;
    private String goodsName;
    private Integer zoneType;
    private Integer isSuit;
    private BigDecimal price;
    private BigDecimal vipPrice;
    private String title;
    private String specification;
    private String hostImg;
    private String details;
    private Integer isDel;
    private Date createTime;
    private Integer repertory;
    private Integer soldNum;
    private String className;
    private Integer sort;
    private List<PGoodsImg> imgList;
}
