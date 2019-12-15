package com.tdpro.entity.extend;

import com.tdpro.common.utils.Page;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@Data
public class VoucherIssueETD extends Page {
    private Long id;

    private Long uid;

    private Long payUid;

    private Long voucherId;
    @ApiModelProperty(value = "券名")
    private String voucherName;
    @ApiModelProperty(value = "0:注册 1:会员购买")
    private Integer type;
    @ApiModelProperty(value = "时间")
    private Date createTime;
    private Long userVoucherId;
    @ApiModelProperty(value = "收益用户")
    private String phone;
}
