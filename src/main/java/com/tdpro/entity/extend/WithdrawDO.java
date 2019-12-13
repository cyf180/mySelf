package com.tdpro.entity.extend;

import com.github.pagehelper.PageInfo;
import com.tdpro.common.utils.QueryModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class WithdrawDO {
    @ApiModelProperty(value = "总申请提现金额")
    private BigDecimal allApplyAmount;
    @ApiModelProperty(value = "总手续费")
    private BigDecimal allPoundageAmount;
    @ApiModelProperty(value = "总实际到账金额")
    private BigDecimal allArrivalAmount;
    private PageInfo pageInfo;
    private QueryModel queryModel;
}
