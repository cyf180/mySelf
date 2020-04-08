package com.tdpro.entity.extend;

import com.tdpro.common.utils.QueryModel;
import lombok.Data;

import java.util.Date;

@Data
public class UserAskPageETD extends QueryModel {
    private Long uid;
    private Long id;
    private String phone;
    private String content;
    private Date createTime;
}
