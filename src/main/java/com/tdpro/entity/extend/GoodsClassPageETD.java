package com.tdpro.entity.extend;

import com.tdpro.common.utils.QueryModel;
import lombok.Data;

import java.util.Date;

@Data
public class GoodsClassPageETD extends QueryModel {
    private Long id;

    private String className;

    private Integer sort;

    private String explain;

    private String iconPath;

    private Integer isShow;

    private Integer isDel;

    private Date createTime;
}
