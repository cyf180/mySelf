package com.tdpro.service;

import com.tdpro.common.utils.Response;
import com.tdpro.entity.PGoodsClass;
import com.tdpro.entity.extend.GoodsClassPageETD;

import java.util.List;

public interface GoodsClassService {
    /**
     * 分类列表
     * @param num
     * @return
     */
    List<PGoodsClass> getList(Integer num);

    /**
     * 后台所有产品分类列表
     * @return
     */
    Response adminClassAllList();

    /**
     * 后台产品分类列表
     * @param classPageETD
     * @return
     */
    Response adminGoodsClassPageList(GoodsClassPageETD classPageETD);

    /**
     * 添加或修改分类
     * @param goodsClass
     * @return
     */
    Response addOrUpdateClass(PGoodsClass goodsClass);

    /**
     * 修改显示隐藏
     * @param goodsClass
     * @return
     */
    Response updateIsShow(PGoodsClass goodsClass);

    /**
     * 详情
     * @param id
     * @return
     */
    Response classInfo(Long id);
}
