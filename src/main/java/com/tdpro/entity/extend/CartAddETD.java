package com.tdpro.entity.extend;

import lombok.Data;

import java.util.List;
@Data
public class CartAddETD {
    private Integer number;

    private List<SuitAddETD> suitList;
}
