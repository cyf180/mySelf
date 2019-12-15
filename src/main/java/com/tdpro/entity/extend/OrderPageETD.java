package com.tdpro.entity.extend;

import com.tdpro.common.utils.QueryModel;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Data
public class OrderPageETD extends QueryModel {
    private Long id;

    private String orderNo;

    private Long uid;

    private Long goodsId;

    private Integer zoneType;

    private Integer isSuit;

    private Integer state;

    private String goodsName;

    private BigDecimal totalPrice;

    private BigDecimal realPrice;

    private Integer number;

    private Integer payType;

    private String receiptName;

    private String receiptPhone;

    private String receiptSite;

    private String suiteName;

    private Integer isDel;

    private Date createTime;

    private Date payTime;

    private String wxOrderNo;

    private BigDecimal callbackPrice;

    private String logisticsName;

    private String logisticsNo;

    private Date seendTime;

    private String userNote;

    private String backNote;

    private Integer isKnot;

    private String userPhone;

    private String startTimes;

    private String endTimes;

    private List<CartETD> cardList;

    private List<OrderVoucherETD> voucherList;

}
