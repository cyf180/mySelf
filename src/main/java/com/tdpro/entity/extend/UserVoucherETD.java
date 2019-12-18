package com.tdpro.entity.extend;

import com.tdpro.common.utils.Page;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class UserVoucherETD extends Page{
    private Long id;
    private Long uid;
    private Long voucherId;
    @ApiModelProperty(value = "使用状态(-1:使用 0:正常)")
    private Integer useState;
    @ApiModelProperty(value = "状态(-1:过期 0:正常)")
    private Integer state;
    @ApiModelProperty(value = "开始时间")
    private Date startTime;
    @ApiModelProperty(value = "到期时间")
    private Date endTime;
    @ApiModelProperty(value = "券名称")
    private String voucherName;
    @ApiModelProperty(value = "面额")
    private BigDecimal faceValue;
    @ApiModelProperty(value = "售价")
    private BigDecimal price;
    @ApiModelProperty(value = "使用门槛(0:无 1:有)")
    private Integer isSill;
    @ApiModelProperty(value = "最低门槛")
    private BigDecimal mainSill;
    @ApiModelProperty(value = "期限类型(0:无 1:固定期限 2:时间区间)")
    private Integer timeType;
    @ApiModelProperty(value = "使用说明")
    private String useExplain;
    @ApiModelProperty(value = "数量")
    private Integer number;
    private Date createTime;
}
