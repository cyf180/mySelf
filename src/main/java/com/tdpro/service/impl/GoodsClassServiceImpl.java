package com.tdpro.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.github.pagehelper.StringUtil;
import com.tdpro.common.utils.Response;
import com.tdpro.common.utils.ResponseUtils;
import com.tdpro.entity.PGoods;
import com.tdpro.entity.PGoodsClass;
import com.tdpro.entity.extend.GoodsClassPageETD;
import com.tdpro.mapper.PGoodsClassMapper;
import com.tdpro.service.GoodsClassService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class GoodsClassServiceImpl implements GoodsClassService {
    @Value("${qiniu.imgPath}")
    private String imgPath;
    @Autowired
    private PGoodsClassMapper goodsClassMapper;

    @Override
    public List<PGoodsClass> getList(Integer num) {
        List<PGoodsClass> classList = goodsClassMapper.selectList(num);
        if (null != classList && classList.size() > 0) {
            for (PGoodsClass goodsClass : classList) {
                if (StringUtil.isNotEmpty(goodsClass.getIconPath())) {
                    goodsClass.setIconPath(imgPath + goodsClass.getIconPath());
                }
            }
        }
        return classList;
    }

    @Override
    public Response adminClassAllList() {
        GoodsClassPageETD classPageETD = new GoodsClassPageETD();
        List<GoodsClassPageETD> list = goodsClassMapper.findGoodsClassPageList(classPageETD);
        return ResponseUtils.successRes(list);
    }

    @Override
    public Response adminGoodsClassPageList(GoodsClassPageETD classPageETD) {
        Integer pageNum = classPageETD.getPageNo() == null ? 1 : classPageETD.getPageNo();
        Integer pageSize = classPageETD.getPageSize() == null ? 10 : classPageETD.getPageSize();
        PageHelper.startPage(pageNum, pageSize);
        List<GoodsClassPageETD> list = goodsClassMapper.findGoodsClassPageList(classPageETD);
        if (null != list && list.size() > 0) {
            for (GoodsClassPageETD classPage : list) {
                if (StringUtil.isNotEmpty(classPage.getIconPath())) {
                    classPage.setIconPath(imgPath + classPage.getIconPath());
                }
            }
        }
        Map map = new HashMap();
        map.put("pageInfo",new PageInfo(list));
        map.put("queryModel",classPageETD);
        return ResponseUtils.successRes(map);
    }
}
