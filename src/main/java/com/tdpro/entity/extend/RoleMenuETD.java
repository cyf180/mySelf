package com.tdpro.entity.extend;

import com.tdpro.common.utils.QueryModel;
import lombok.Data;

@Data
public class RoleMenuETD extends QueryModel {
    private Integer id;

    private Integer rid;

    private Integer mid;
}