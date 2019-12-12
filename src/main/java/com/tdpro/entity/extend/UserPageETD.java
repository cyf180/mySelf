package com.tdpro.entity.extend;

import com.tdpro.common.utils.QueryModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class UserPageETD extends QueryModel {
    private Long id;
    @ApiModelProperty(value = "是否是会员(0:不是 1:是)")
    private Integer isUser;
    @ApiModelProperty(value = "手机号")
    private String phone;
    private String payPassword;
    @ApiModelProperty(value = "-1:禁用 0:正常")
    private Integer state;
    @ApiModelProperty(value = "推荐人uid")
    private Long strawUid;
    @ApiModelProperty(value = "昵称")
    private String nickName;
    @ApiModelProperty(value = "真实姓名")
    private String name;
    @ApiModelProperty(value = "余额")
    private BigDecimal balance;
    @ApiModelProperty(value = "积分")
    private BigDecimal integral;
    @ApiModelProperty(value = "总积分")
    private BigDecimal totalIntegral;
    @ApiModelProperty(value = "开户行")
    private String bankName;
    @ApiModelProperty(value = "开户支行")
    private String bankBranch;
    @ApiModelProperty(value = "银行卡号")
    private String bankCard;
    @ApiModelProperty(value = "身份证")
    private String idCard;
    @ApiModelProperty(value = "创建时间")
    private Date createTime;
    @ApiModelProperty(value = "禁用时间")
    private Date disableTime;
    @ApiModelProperty(value = "解禁时间")
    private Date uncoilTime;
    private String strawPath;
    @ApiModelProperty(value = "结算利率id")
    private Long knotId;
    @ApiModelProperty(value = "结算总金额")
    private BigDecimal knotAmount;
    @ApiModelProperty(value = "团队单品数")
    private Integer teamOneNum;
    @ApiModelProperty(value = "团队套装数")
    private Integer teamSuitNum;
    private BigDecimal itemBuyAmount;
    private BigDecimal itemLeftAmount;
    private Integer suitLevelNum;
    private BigDecimal rate;
    private String strawPhone;
}
