package com.tdpro.entity.extend;

import com.tdpro.entity.PGoodsExchange;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class OrderCartETD {
    @ApiModelProperty(value = "商品id")
    private Long goodsId;
    @ApiModelProperty(value = "收货地址id")
    private Long siteId;
    private Long uid;
    @ApiModelProperty(value = "默认收货地址实体")
    private UserSiteETD userSite;
    @ApiModelProperty(value = "商品名称")
    private String goodsName;
    @ApiModelProperty(value = "商品图片")
    private String hostImg;
    @ApiModelProperty(value = "价格")
    private BigDecimal price;
    @ApiModelProperty(value = "专区类型(0:普通专区 1:会员专区 2:兑换专区)")
    private Integer zoneType;
    @ApiModelProperty(value = "商品优惠券配置列表")
    List<GoodsExchangeETD> goodsExchangeList;
    @ApiModelProperty(value = "")
    private String userNote;
    @ApiModelProperty(value = "规格配置列表")
    List<GoodsSuitETD> suitList;
    private Long voucherId;
}
