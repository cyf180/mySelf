package com.tdpro.entity.extend;

import com.tdpro.common.utils.QueryModel;
import lombok.Data;

import java.util.Date;

@Data
public class AdminPageETD extends QueryModel {
    private Long id;

    private String phone;

    private String name;

    private Integer six;

    private String password;

    private Long rid;

    private Integer state;

    private Date disableTime;

    private Date liftingTime;

    private Date createTime;

    private String roleRank;
}
