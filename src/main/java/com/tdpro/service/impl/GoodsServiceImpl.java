package com.tdpro.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.github.pagehelper.StringUtil;
import com.tdpro.common.utils.Response;
import com.tdpro.common.utils.ResponseUtils;
import com.tdpro.entity.PGoodsImg;
import com.tdpro.entity.extend.GoodsETD;
import com.tdpro.entity.extend.GoodsSuitETD;
import com.tdpro.mapper.PGoodsImgMapper;
import com.tdpro.mapper.PGoodsMapper;
import com.tdpro.mapper.PGoodsSuitMapper;
import com.tdpro.service.GoodsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GoodsServiceImpl implements GoodsService {
    @Value("${qiniu.imgPath}")
    private String imgPath;
    @Autowired
    private PGoodsMapper goodsMapper;
    @Autowired
    private PGoodsImgMapper goodsImgMapper;
    @Autowired
    private PGoodsSuitMapper goodsSuitMapper;
    @Override
    public List<GoodsETD> goodsList(GoodsETD goodsETD){
        Integer pageNo = goodsETD.getPageNo() == null ? 1 : goodsETD.getPageNo();
        Integer pageSize = goodsETD.getPageSize() == null ? 10: goodsETD.getPageSize();
        PageHelper.startPage(pageNo,pageSize);
        List<GoodsETD> goodsList = goodsMapper.pageList(goodsETD);
        if(null != goodsList && goodsList.size() >0){
            for (GoodsETD goods:goodsList){
                if(!StringUtil.isEmpty(goods.getHostImg())){
                    goods.setHostImg(imgPath+goods.getHostImg());
                }
            }
        }
        return goodsList;
    }

    @Override
    public Response goodsInfo(GoodsETD goodsETD){
        if(null == goodsETD.getId()){
            return ResponseUtils.errorRes("关键之错误");
        }
        GoodsETD goodsInfo = goodsMapper.selectInfo(goodsETD);
        if(null == goodsInfo){
            return ResponseUtils.errorRes("产品已下架");
        }
        List<GoodsSuitETD> suitList = goodsSuitMapper.selectAllListByGoodsId(goodsInfo.getId());
        if(null != suitList  && suitList.size() > 0){
            goodsInfo.setSuitList(suitList);
        }
        List<PGoodsImg> imgList = goodsImgMapper.selectListByGoodsId(goodsInfo.getId());
        if(null != imgList && imgList.size() >0){
            for (PGoodsImg img:imgList){
                if(StringUtil.isNotEmpty(img.getImgPath())){
                    img.setImgPath(imgPath+img.getImgPath());
                }
            }
            goodsInfo.setImgList(imgList);
        }else{
            if(StringUtil.isNotEmpty(goodsInfo.getHostImg())) {
                PGoodsImg img = new PGoodsImg();
                img.setGoodsId(goodsInfo.getId());
                img.setImgPath(imgPath + goodsInfo.getHostImg());
            }
        }
        return ResponseUtils.successRes(goodsInfo);
    }
}
