package com.tdpro.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.github.pagehelper.StringUtil;
import com.tdpro.common.exception.BusinessException;
import com.tdpro.common.utils.Response;
import com.tdpro.common.utils.ResponseUtils;
import com.tdpro.entity.*;
import com.tdpro.entity.extend.*;
import com.tdpro.mapper.*;
import com.tdpro.service.GoodsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

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
    @Autowired
    private PGoodsClassMapper classMapper;
    Lock isDelLock = new ReentrantLock();
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

    @Override
    public Boolean updateRepertory(POrder order) {
        PGoods goods = goodsMapper.selectByPrimaryKey(order.getGoodsId());
        if(null == goods){
            return false;
        }
        int newRepertory = goods.getRepertory()-order.getNumber();
        int newSoldNum = goods.getSoldNum()+order.getNumber();
        if(newRepertory < 0){
            newRepertory = 0;
        }
        GoodsRepertoryUpdateETD repertoryUpdateUPD = new GoodsRepertoryUpdateETD();
        repertoryUpdateUPD.setId(goods.getId());
        repertoryUpdateUPD.setRepertory(newRepertory);
        repertoryUpdateUPD.setSoldNum(newSoldNum);
        repertoryUpdateUPD.setOldRepertory(goods.getRepertory());
        if(0 == goodsMapper.updateRepertory(repertoryUpdateUPD)){
            return false;
        }
        return true;
    }

    @Override
    public Response adminPageList(GoodsPageETD goodsPageETD) {
        int zoneType = goodsPageETD.getZoneType() == null ? 0 : goodsPageETD.getZoneType();
        goodsPageETD.setZoneType(zoneType);
        Integer pageNum = goodsPageETD.getPageNo() == null ? 1 : goodsPageETD.getPageNo();
        Integer pageSize = goodsPageETD.getPageSize() == null ? 10 : goodsPageETD.getPageSize();
        PageHelper.startPage(pageNum, pageSize);
        List<GoodsPageETD> list = goodsMapper.adminPageList(goodsPageETD);
        if(null != list && list.size() >0){
            for (GoodsPageETD goods:list){
                if(StringUtil.isNotEmpty(goods.getHostImg())){
                    goods.setHostImg(imgPath+goods.getHostImg());
                }
            }
        }
        Map map = new HashMap();
        map.put("pageInfo",new PageInfo(list));
        map.put("queryModel",goodsPageETD);
        return ResponseUtils.successRes(map);
    }

    @Override
    @Transactional(rollbackFor = Exception.class, isolation = Isolation.READ_COMMITTED)
    public Response updateAndAddGoods(GoodsInfoETD goodsInfoETD){
        PGoods goodsADD=new PGoods();
        Integer zoneType = goodsInfoETD.getZoneType() == null ? 0 :  goodsInfoETD.getZoneType();
        if(null == goodsInfoETD.getClassId()){
            return ResponseUtils.errorRes("分类不能为空");
        }
        PGoodsClass pGoodsClass = classMapper.selectByPrimaryKey(goodsInfoETD.getClassId());
        if(null == pGoodsClass){
            return ResponseUtils.errorRes("分类不存在");
        }
        if(StringUtil.isEmpty(goodsInfoETD.getGoodsName())){
            return ResponseUtils.errorRes("产品名称不能为空");
        }
        if(null == goodsInfoETD.getZoneType()){
            return ResponseUtils.errorRes("请选择商品区域");
        }
        if(null == goodsInfoETD.getRepertory() || goodsInfoETD.getRepertory().compareTo(new Integer(0)) <= 0){
            return ResponseUtils.errorRes("库存错误");
        }
        if(!zoneType.equals(2)) {
            if (null == goodsInfoETD.getPrice() || goodsInfoETD.getPrice().compareTo(new BigDecimal("0")) < 0) {
                return ResponseUtils.errorRes("商品价格有误");
            }
        }else{
            if((null == goodsInfoETD.getSixCouponNum() && null == goodsInfoETD.getThreeCouponNum()) ||
                    (goodsInfoETD.getSixCouponNum().compareTo(new Integer(0)) <= 0  &&  goodsInfoETD.getThreeCouponNum().compareTo(new Integer(0)) <= 0)){
                return ResponseUtils.errorRes("请输入兑换券数量");
            }
        }
        if(StringUtil.isEmpty(goodsInfoETD.getSpecification())){
            return ResponseUtils.errorRes("请输规格");
        }
        if(StringUtil.isEmpty(goodsInfoETD.getHostImg())){
            return ResponseUtils.errorRes("请上传主图");
        }
        if(StringUtil.isEmpty(goodsInfoETD.getDetails())){
            return ResponseUtils.errorRes("请输入产品详情");
        }
        if(StringUtil.isNotEmpty(goodsInfoETD.getTitle())){
            goodsADD.setTitle(goodsInfoETD.getTitle());
        }
        Integer sort = goodsInfoETD.getSort() == null ? 0 :  goodsInfoETD.getSort();
        Integer isSuit = goodsInfoETD.getIsSuit() == null ? 0 :  goodsInfoETD.getIsSuit();
        goodsADD.setSort(sort);
        goodsADD.setIsSuit(isSuit);
        goodsADD.setCreateTime(new Date());
        goodsADD.setZoneType(zoneType);
        goodsADD.setGoodsName(goodsInfoETD.getGoodsName());
        goodsADD.setClassId(goodsInfoETD.getClassId());
        goodsADD.setPrice(goodsInfoETD.getPrice());
        goodsADD.setSpecification(goodsInfoETD.getSpecification());
        goodsADD.setHostImg(goodsInfoETD.getHostImg());
        goodsADD.setDetails(goodsInfoETD.getDetails());
        goodsADD.setRepertory(goodsInfoETD.getRepertory());
        if(0 == goodsMapper.insertSelective(goodsADD)){
            throw new BusinessException("添加产品失败");
        }
        Long goodsId = goodsADD.getId();
        if(!this.addImgList(goodsInfoETD.getProductImgs(),goodsId)){
            throw new BusinessException("添加产品图片失败");
        }
        if(goodsADD.getIsSuit().equals(new Integer(1))){
            if(!this.addGoodsSuit(goodsInfoETD.getExplain(),goodsId)){
                throw new BusinessException("添加产品规格失败");
            }
        }
        if(!this.addExchange(goodsInfoETD,goodsId)){
            throw new BusinessException("添加兑换产品配置失败");
        }
        return ResponseUtils.successRes(1);
    }

    @Override
    @Transactional(rollbackFor = Exception.class, isolation = Isolation.READ_COMMITTED)
    public Response updateGoods(GoodsInfoETD goodsInfoETD){
        if(null == goodsInfoETD.getId()){
            return ResponseUtils.errorRes("关键之错误");
        }
        PGoods goodsFind = goodsMapper.selectByPrimaryKey(goodsInfoETD.getId());
        if(null == goodsFind){
            return ResponseUtils.errorRes("产品不存在");
        }
        PGoods goodsUPD = new PGoods();
        goodsUPD.setId(goodsFind.getId());
        if(null != goodsInfoETD.getClassId() && !goodsInfoETD.getClassId().equals(goodsFind.getClassId())){
            PGoodsClass classFind = classMapper.selectByPrimaryKey(goodsInfoETD.getId());
            if(null == classFind){
                return ResponseUtils.errorRes("分类不存在");
            }
            goodsUPD.setClassId(classFind.getId());
        }
        if(StringUtil.isNotEmpty(goodsInfoETD.getGoodsName()) && !goodsInfoETD.getGoodsName().equals(goodsFind.getGoodsName())){
            goodsUPD.setGoodsName(goodsInfoETD.getGoodsName());
        }
        if(null != goodsInfoETD.getZoneType() && !goodsInfoETD.getZoneType().equals(goodsFind.getZoneType())){
            goodsUPD.setZoneType(goodsInfoETD.getZoneType());
        }
        if(null != goodsInfoETD.getIsSuit() && !goodsInfoETD.getIsSuit().equals(goodsFind.getIsSuit())){
            goodsUPD.setIsSuit(goodsInfoETD.getIsSuit());
        }
        if(null != goodsInfoETD.getPrice()){
            if(goodsInfoETD.getPrice().compareTo(new BigDecimal("0")) < 0){
                return ResponseUtils.errorRes("价格错误");
            }
            goodsUPD.setPrice(goodsInfoETD.getPrice());
        }
        if(StringUtil.isNotEmpty(goodsInfoETD.getTitle())){
            goodsUPD.setTitle(goodsInfoETD.getTitle());
        }
        if(StringUtil.isNotEmpty(goodsInfoETD.getSpecification())){
            goodsUPD.setSpecification(goodsInfoETD.getSpecification());
        }
        if(StringUtil.isNotEmpty(goodsInfoETD.getHostImg())){
            goodsUPD.setHostImg(goodsInfoETD.getHostImg());
        }
        if(StringUtil.isNotEmpty(goodsInfoETD.getDetails())){
            goodsUPD.setDetails(goodsInfoETD.getDetails());
        }
        if(null != goodsInfoETD.getSort()){
            goodsUPD.setSort(goodsInfoETD.getSort());
        }
        if(null != goodsInfoETD.getRepertory() && goodsInfoETD.getRepertory().compareTo(new Integer(0)) > 0){
            goodsUPD.setRepertory(goodsInfoETD.getRepertory());
        }
        if(0 == goodsMapper.updateByPrimaryKeySelective(goodsUPD)){
            throw new BusinessException("修改失败");
        }
        return ResponseUtils.successRes(1);
    }

    @Override
    public Response updateGoodsIsDel(Long id){
        try {
            isDelLock.lock();
            if(null == id){
                return ResponseUtils.errorRes("关键之错误");
            }
            PGoods goodsFind = goodsMapper.selectByPrimaryKey(id);
            if(null == goodsFind){
                return ResponseUtils.errorRes("产品不存在");
            }
            int isDel = 0;
            if(goodsFind.getIsDel().equals(new Integer(0))){
                isDel = -1;
            }
            PGoods goodsUPD = new PGoods();
            goodsUPD.setId(goodsFind.getId());
            goodsUPD.setIsDel(isDel);
            if(0 == goodsMapper.updateByPrimaryKeySelective(goodsUPD)){
                throw new BusinessException("失败");
            }
        }catch (Exception e){
            throw new BusinessException(e.getMessage());
        }finally {
            isDelLock.unlock();
        }
        return ResponseUtils.successRes(1);
    }

    private boolean addExchange(GoodsInfoETD goodsInfoETD,Long goodsId){
        if(goodsInfoETD.getZoneType().equals(new Integer(2))){
            if(null != goodsInfoETD.getSixCouponNum() && new Integer(0).compareTo(goodsInfoETD.getSixCouponNum()) < 0){
                if(!this.insertExchange(goodsId,goodsInfoETD.getSixCouponNum(),1L)){
                    return false;
                }
            }
            if(null != goodsInfoETD.getThreeCouponNum() && new Integer(0).compareTo(goodsInfoETD.getThreeCouponNum()) < 0){
                if(!this.insertExchange(goodsId,goodsInfoETD.getThreeCouponNum(),2L)){
                    return false;
                }
            }
        }
        return true;
    }

    private boolean insertExchange(Long goodsId,Integer number,Long vId){
        PGoodsExchange exchangeADD = new PGoodsExchange();
        exchangeADD.setGoodsId(goodsId);
        exchangeADD.setVoucherId(vId);
        exchangeADD.setNumber(number);
        if(0 == exchangeMapper.insertSelective(exchangeADD)){
            return false;
        }
        return true;
    }

    private boolean addGoodsSuit(String explain,Long goodsId){
        if(StringUtil.isNotEmpty(explain)) {
            String[] explainList = explain.split("@");
            if(explainList.length > 0) {
                for (String exp : explainList) {
                    PGoodsSuit goodsSuitADD = new PGoodsSuit();
                    goodsSuitADD.setGoodsId(goodsId);
                    goodsSuitADD.setExplain(exp);
                    goodsSuitADD.setCreateTime(new Date());
                    if(0 == goodsSuitMapper.insertSelective(goodsSuitADD)){
                        return false;
                    }
                }
            }
        }
        return true;
    }

    private boolean addImgList(String productImgs,Long goodsId){
        if(StringUtil.isNotEmpty(productImgs)) {
            String[] imgList = productImgs.split(",");
            if(imgList.length > 0) {
                for (String imgPath : imgList) {
                    String[] pathArr = imgPath.split("/");
                    String path = "";
                    if (pathArr.length > 0) {
                        path = pathArr[pathArr.length - 1];
                        PGoodsImg imgADD = new PGoodsImg();
                        imgADD.setImgPath(path);
                        imgADD.setGoodsId(goodsId);
                        if(0 == goodsImgMapper.insertSelective(imgADD)){
                            return false;
                        }
                    }
                }
            }
        }
        return true;
    }
}
