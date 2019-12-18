package com.tdpro.service.impl;

import com.github.pagehelper.StringUtil;
import com.tdpro.common.QiniuSimpleUpload;
import com.tdpro.entity.PGoodsImg;
import com.tdpro.mapper.PGoodsImgMapper;
import com.tdpro.service.GoodsImgService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GoodsImgServiceImpl implements GoodsImgService {
    @Autowired
    private PGoodsImgMapper imgMapper;

    @Override
    public Boolean delById(Long id) {
        PGoodsImg img = imgMapper.selectByPrimaryKey(id);
        if(null == img){
            return false;
        }
        if(StringUtil.isNotEmpty(img.getImgPath())){
            QiniuSimpleUpload qiniuSimpleUpload=new QiniuSimpleUpload();
            qiniuSimpleUpload.delete(img.getImgPath());
        }
        if(0==imgMapper.deleteByPrimaryKey(id)){
            return false;
        }
        return true;
    }
}
