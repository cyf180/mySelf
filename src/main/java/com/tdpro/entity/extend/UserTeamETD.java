package com.tdpro.entity.extend;

import com.tdpro.common.utils.Page;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class UserTeamETD extends Page {
    private Long id;
    private Long strawUid;
    @ApiModelProperty(value = "手机号")
    private String phone;
    @ApiModelProperty(value = "余额")
    private BigDecimal balance;
    @ApiModelProperty(value = "积分")
    private BigDecimal integral;
    @ApiModelProperty(value = "昵称")
    private String nickName;
    @ApiModelProperty(value = "头像")
    private String headPath;
    @ApiModelProperty(value = "购买订单数量")
    private Integer payOrderNum;
    @ApiModelProperty(value = "是否是会员(0:不是 1:是)")
    private Integer isUser;
}
