package com.tdpro.entity.extend;

import com.tdpro.entity.PAdvert;
import com.tdpro.entity.PGoodsClass;
import lombok.Data;

import java.util.List;

@Data
public class HomeETD {
    private List<PAdvert> advertList;
    private List<PGoodsClass> classList;
    private List<GoodsETD> goodsList;
}
