package com.tdpro.entity.extend;

import lombok.Data;

@Data
public class GoodsExchangeETD {
    private Long id;
    private Long goodsId;
    private Long voucherId;
    private Integer number;
    private String voucherName;
}
