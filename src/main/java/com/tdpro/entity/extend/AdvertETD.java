package com.tdpro.entity.extend;

import com.tdpro.common.utils.QueryModel;
import lombok.Data;

import java.util.Date;

@Data
public class AdvertETD extends QueryModel {
    private Long id;

    private String title;

    private Integer sort;

    private String imgPath;

    private String note;

    private Integer isDel;

    private Date createTime;
}
