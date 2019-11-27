package com.tdpro.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.github.pagehelper.StringUtil;
import com.tdpro.common.utils.Response;
import com.tdpro.common.utils.ResponseUtils;
import com.tdpro.entity.PGoodsExchange;
import com.tdpro.entity.PGoodsImg;
import com.tdpro.entity.extend.*;
import com.tdpro.mapper.*;
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
    @Autowired
    private PUserSiteMapper userSiteMapper;
    @Autowired
    private PGoodsExchangeMapper exchangeMapper;

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

    @Override
    public Response goodsAffirm(Long id,Long uid) {
        if(null == uid){
            return ResponseUtils.errorRes("请先登录");
        }
        if(null == id){
            return ResponseUtils.errorRes("产品信息错误");
        }
        OrderCartETD orderCart = goodsMapper.selectAffirmById(id);
        if(null == orderCart){
            return ResponseUtils.errorRes("商品已下架");
        }
        UserSiteETD userSite = userSiteMapper.selectOneByUid(uid);
        if(null != userSite){
            orderCart.setUserSite(userSite);
        }
        List<GoodsExchangeETD> exchangeList = exchangeMapper.selectListByGoodsId(orderCart.getGoodsId());
        if(null != exchangeList && exchangeList.size() > 0){
            orderCart.setGoodsExchangeList(exchangeList);
        }
        return ResponseUtils.successRes(orderCart);
    }

    @Override
    public GoodsETD selectInfo(Long id) {
        GoodsETD goodsWhere = new GoodsETD();
        goodsWhere.setId(id);
        return goodsMapper.selectInfo(goodsWhere);
    }
}
