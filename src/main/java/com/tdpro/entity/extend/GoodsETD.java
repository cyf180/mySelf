package com.tdpro.entity.extend;

import com.tdpro.common.utils.Page;
import com.tdpro.entity.PGoodsImg;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Data
public class GoodsETD extends Page {
    private Long id;
    private Long classId;
    @ApiModelProperty(value = "商品名称")
    private String goodsName;
    @ApiModelProperty(value = "专区类型(0:普通专区 1:会员专区 2:兑换专区)")
    private Integer zoneType;
    @ApiModelProperty(value = "是否是套装(0:不是 1:是)")
    private Integer isSuit;
    @ApiModelProperty(value = "商品价格")
    private BigDecimal price;
    @ApiModelProperty(value = "商品简介")
    private String title;
    @ApiModelProperty(value = "单位")
    private String specification;
    @ApiModelProperty(value = "主图")
    private String hostImg;
    @ApiModelProperty(value = "商品详情")
    private String details;
    private Integer isDel;
    @ApiModelProperty(value = "时间")
    private Date createTime;
    private Integer repertory;
    private Integer soldNum;
    @ApiModelProperty(value = "0:未收藏 1:已收藏")
    private Integer isPut = new Integer(0);
    @ApiModelProperty(value = "规格列表(详情使用)")
    List<GoodsSuitETD> suitList;
    @ApiModelProperty(value = "图片列表(详情使用)")
    List<PGoodsImg> imgList;
}
