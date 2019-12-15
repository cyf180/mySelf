package com.tdpro.entity.extend;

import com.tdpro.entity.PGoodsImg;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Data
public class GoodsInfoETD {
    private Long id;
    private Long classId;
    private String goodsName;
    private Integer zoneType;
    private Integer isSuit;
    private BigDecimal price;
    private String title;
    private String specification;
    private String hostImg;
    private String details;
    private Integer isDel;
    private Date createTime;
    private Integer repertory;
    private Integer soldNum;
    private Integer sort;
    private String explain;
    private String productImgs;
    private Integer sixCouponNum;
    private Integer threeCouponNum;
    private List<PGoodsImg> imgList;
}
