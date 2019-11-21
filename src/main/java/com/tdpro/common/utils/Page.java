package com.tdpro.common.utils;

import lombok.Data;

@Data
public class Page<T> {
    private Integer pageNo;
    private Integer pageSize;
    private Integer total;
    T list;
}
