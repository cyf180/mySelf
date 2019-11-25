package com.tdpro.entity.extend;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@Data
public class GoodsSuitETD {
    private Long id;
    private Long goodsId;
    @ApiModelProperty(value = "名称（说明）")
    private String explain;
    private Date createTime;
    private Integer number;
}
