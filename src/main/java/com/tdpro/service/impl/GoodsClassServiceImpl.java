package com.tdpro.service.impl;

import com.github.pagehelper.StringUtil;
import com.tdpro.entity.PGoods;
import com.tdpro.entity.PGoodsClass;
import com.tdpro.mapper.PGoodsClassMapper;
import com.tdpro.service.GoodsClassService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GoodsClassServiceImpl implements GoodsClassService {
    @Value("${qiniu.imgPath}")
    private String imgPath;
    @Autowired
    private PGoodsClassMapper goodsClassMapper;
    @Override
    public List<PGoodsClass> getList(Integer num){
        List<PGoodsClass> classList = goodsClassMapper.selectList(num);
        if(null != classList && classList.size() >0){
            for (PGoodsClass goodsClass:classList){
                if(StringUtil.isNotEmpty(goodsClass.getIconPath())){
                    goodsClass.setIconPath(imgPath+goodsClass.getIconPath());
                }
            }
        }
        return classList;
    }
}
