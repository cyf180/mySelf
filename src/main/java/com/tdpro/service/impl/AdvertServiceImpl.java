package com.tdpro.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.github.pagehelper.StringUtil;
import com.tdpro.common.exception.BusinessException;
import com.tdpro.common.utils.Response;
import com.tdpro.common.utils.ResponseUtils;
import com.tdpro.entity.PAdvert;
import com.tdpro.entity.extend.AdvertETD;
import com.tdpro.entity.extend.OrderPageETD;
import com.tdpro.mapper.PAdvertMapper;
import com.tdpro.service.AdvertService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    @Override
    public Response adminPageList(AdvertETD advertETD){
        Integer pageNum = advertETD.getPageNo() == null ? 1 : advertETD.getPageNo();
        Integer pageSize = advertETD.getPageSize() == null ? 10 : advertETD.getPageSize();
        PageHelper.startPage(pageNum, pageSize);
        List<AdvertETD> list = advertMapper.findPageList(advertETD);
        if(null != list && list.size() > 0){
            for (AdvertETD advert:list){
                if(StringUtil.isNotEmpty(advert.getImgPath())){
                    advert.setImgPath(imgPath+advert.getImgPath());
                }
            }
        }
        Map map = new HashMap();
        map.put("pageInfo",new PageInfo(list));
        map.put("queryModel",advertETD);
        return ResponseUtils.successRes(map);
    }

    @Override
    public Response adminAdvertInfo(Long id){
        AdvertETD advertETD = advertMapper.findInfoById(id);
        if(null != advertETD && StringUtil.isNotEmpty(advertETD.getImgPath())){
            advertETD.setImgPath(imgPath+advertETD.getImgPath());
        }
        return ResponseUtils.successRes(advertETD);
    }

    @Override
    public Response updateAdvert(PAdvert advert){
        if(StringUtil.isEmpty(advert.getImgPath())){
            return ResponseUtils.errorRes("图片错误");
        }
        Integer sort = advert.getSort() == null ? 0 : advert.getSort();
        PAdvert advertUPD = new PAdvert();
        advertUPD.setSort(sort);
        advertUPD.setImgPath(advert.getImgPath());
        if(StringUtil.isNotEmpty(advert.getTitle())){
            advertUPD.setTitle(advert.getTitle());
        }
        if(StringUtil.isNotEmpty(advert.getNote())){
            advertUPD.setNote(advert.getNote());
        }
        if(null == advert.getId()){
            advertUPD.setCreateTime(new Date());
            if(0 == advertMapper.insertSelective(advertUPD)){
                throw new BusinessException("添加失败");
            }
        }else{
            PAdvert advertFid = advertMapper.selectByPrimaryKey(advert.getId());
            if(null == advertFid){
                return ResponseUtils.errorRes("广告不存在");
            }
            advertUPD.setId(advertFid.getId());
            if(0 == advertMapper.updateByPrimaryKeySelective(advertUPD)){
                throw new BusinessException("修改失败");
            }
        }
        return ResponseUtils.successRes(1);
    }

    @Override
    public Response deleteById(Long id){
        if(0 == advertMapper.deleteByPrimaryKey(id)){
            throw new BusinessException("删除失败");
        }
        return ResponseUtils.successRes(1);
    }
}
