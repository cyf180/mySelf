package com.tdpro.entity.extend;

import com.tdpro.common.utils.Page;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@Data
public class UserSiteETD extends Page {
    private Long id;
    private Long uid;
    @ApiModelProperty(value = "收件人名称")
    private String name;
    @ApiModelProperty(value = "手机")
    private String phone;
    @ApiModelProperty(value = "地址")
    private String site;
    @ApiModelProperty(value = "是否默认(0:否 1:是)")
    private Integer isDefault;
    private Date createTime;
}
