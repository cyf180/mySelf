package com.tdpro.service.impl;

import com.github.pagehelper.StringUtil;
import com.tdpro.entity.PAdvert;
import com.tdpro.mapper.PAdvertMapper;
import com.tdpro.service.AdvertService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdvertServiceImpl implements AdvertService {
    @Value("${qiniu.imgPath}")
    private String imgPath;
    @Autowired
    private PAdvertMapper advertMapper;
    @Override
    public List<PAdvert> advertList(){
        List<PAdvert> list = advertMapper.selectList();
        if(null != list && list.size() > 0){
            for (PAdvert advert:list){
                if(StringUtil.isNotEmpty(advert.getImgPath())){
                    advert.setImgPath(imgPath+advert.getImgPath());
                }
            }
        }
        return list;
    }
}
