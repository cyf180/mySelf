package com.tdpro.entity.extend;

import com.tdpro.entity.PGoodsExchange;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class OrderCartETD {
    private Long goodsId;
    private Long siteId;
    private Long uid;
    private UserSiteETD userSite;
    private String goodsName;
    private String hostImg;
    private BigDecimal price;
    private Integer zoneType;
    List<GoodsExchangeETD> goodsExchangeList;
    private String userNote;
    private Integer number;
    List<GoodsSuitETD> suitList;
}
