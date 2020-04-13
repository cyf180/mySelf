package com.tdpro.entity.extend;

import com.tdpro.entity.PGoodsExchange;
import com.tdpro.entity.PVoucher;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class OrderCartETD {
    @ApiModelProperty(value = "商品id")
    private Long goodsId;
    private Long uid;
    private Long orderId;
    @ApiModelProperty(value = "订单总价格")
    private BigDecimal totalPrice;
    @ApiModelProperty(value = "专区类型(0:普通专区 1:会员专区 2:兑换专区)")
    private Integer zoneType;
    @ApiModelProperty(value = "默认收货地址实体")
    private UserSiteETD userSite;
    @ApiModelProperty(value = "订单总数量")
    private Integer orderNum;
    @ApiModelProperty(value = "商品优惠券配置列表")
    private List<GoodsExchangeETD> goodsExchangeList;
    @ApiModelProperty(value = "规格配置列表")
    private List<GoodsSuitETD> suitList;
    @ApiModelProperty(value = "订单产品列表")
    private List<CartETD> cartList;
    private PVoucher voucher;
}
