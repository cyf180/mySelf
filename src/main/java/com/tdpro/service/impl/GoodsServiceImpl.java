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
import com.tdpro.service.UserService;
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
    @Autowired
    private POrderMapper orderMapper;
    @Autowired
    private PCartMapper cartMapper;
    @Autowired
    private PCollectMapper collectMapper;
    Lock isDelLock = new ReentrantLock();

    @Override
    public List<GoodsETD> goodsList(GoodsETD goodsETD) {
        Integer pageNo = goodsETD.getPageNo() == null ? 1 : goodsETD.getPageNo();
        Integer pageSize = goodsETD.getPageSize() == null ? 10 : goodsETD.getPageSize();
        PageHelper.startPage(pageNo, pageSize);
        List<GoodsETD> goodsList = goodsMapper.pageList(goodsETD);
        if (null != goodsList && goodsList.size() > 0) {
            for (GoodsETD goods : goodsList) {
                if (!StringUtil.isEmpty(goods.getHostImg())) {
                    goods.setHostImg(imgPath + goods.getHostImg());
                }
            }
        }
        return goodsList;
    }

    @Override
    public Response goodsInfo(GoodsETD goodsETD, Long uid) {
        if (null == goodsETD.getId()) {
            return ResponseUtils.errorRes("关键之错误");
        }
        GoodsETD goodsInfo = goodsMapper.selectInfo(goodsETD);
        if (null == goodsInfo) {
            return ResponseUtils.errorRes("产品已下架");
        }
        if(StringUtil.isNotEmpty(goodsInfo.getHostImg())){
            goodsInfo.setHostImg(imgPath+goodsInfo.getHostImg());
        }
        if(goodsInfo.getIsSuit().equals(new Integer(1))) {
            List<GoodsSuitETD> suitList = goodsSuitMapper.selectAllListByGoodsId(goodsInfo.getId());
            if (null != suitList && suitList.size() > 0) {
                goodsInfo.setSuitList(suitList);
            }
        }
        if (!new Integer(0).equals(uid)) {
            PCollect collect = collectMapper.findByUidAndGoodsId(uid, goodsInfo.getId());
            if (null != collect) {
                goodsInfo.setIsPut(1);
            }
        }
        List<PGoodsImg> imgList = goodsImgMapper.selectListByGoodsId(goodsInfo.getId());
        if (null != imgList && imgList.size() > 0) {
            for (PGoodsImg img : imgList) {
                if (StringUtil.isNotEmpty(img.getImgPath())) {
                    img.setImgPath(imgPath + img.getImgPath());
                }
            }
            goodsInfo.setImgList(imgList);
        } else {
            if (StringUtil.isNotEmpty(goodsInfo.getHostImg())) {
                PGoodsImg img = new PGoodsImg();
                img.setGoodsId(goodsInfo.getId());
                img.setImgPath(imgPath + goodsInfo.getHostImg());
            }
        }
        return ResponseUtils.successRes(goodsInfo);
    }

    @Override
    public Response goodsAffirm(Long id, Long uid) {
        if (null == id) {
            return ResponseUtils.errorRes("产品信息错误");
        }
        OrderCartETD orderCart = orderMapper.selectAffirmById(id);
        if (null == orderCart) {
            return ResponseUtils.errorRes("订单不存在或不属于待支付");
        }
        UserSiteETD userSite = userSiteMapper.selectOneByUid(uid);
        if (null != userSite) {
            orderCart.setUserSite(userSite);
        }
        List<GoodsExchangeETD> exchangeList = exchangeMapper.selectListByGoodsIdAndUid(orderCart.getGoodsId(), uid);
        if (null != exchangeList && exchangeList.size() > 0) {
            orderCart.setGoodsExchangeList(exchangeList);
        }
        List<CartETD> cartList = cartMapper.findListByOrderId(orderCart.getOrderId());
        if (null != cartList) {
            for (CartETD cart : cartList) {
                if (StringUtil.isNotEmpty(cart.getHostImg())) {
                    cart.setHostImg(imgPath + cart.getHostImg());
                }
            }
            orderCart.setCartList(cartList);
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
        if (null == goods) {
            return false;
        }
        int newRepertory = goods.getRepertory() - order.getNumber();
        int newSoldNum = goods.getSoldNum() + order.getNumber();
        if (newRepertory < 0) {
            newRepertory = 0;
        }
        GoodsRepertoryUpdateETD repertoryUpdateUPD = new GoodsRepertoryUpdateETD();
        repertoryUpdateUPD.setId(goods.getId());
        repertoryUpdateUPD.setRepertory(newRepertory);
        repertoryUpdateUPD.setSoldNum(newSoldNum);
        repertoryUpdateUPD.setOldRepertory(goods.getRepertory());
        if (0 == goodsMapper.updateRepertory(repertoryUpdateUPD)) {
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
        if (null != list && list.size() > 0) {
            for (GoodsPageETD goods : list) {
                if (StringUtil.isNotEmpty(goods.getHostImg())) {
                    goods.setHostImg(imgPath + goods.getHostImg());
                }
            }
        }
        Map map = new HashMap();
        map.put("pageInfo", new PageInfo(list));
        map.put("queryModel", goodsPageETD);
        return ResponseUtils.successRes(map);
    }

    @Override
    @Transactional(rollbackFor = Exception.class, isolation = Isolation.READ_COMMITTED)
    public Response addGoods(GoodsInfoETD goodsInfoETD) {
        PGoods goodsADD = new PGoods();
        Integer zoneType = goodsInfoETD.getZoneType() == null ? 0 : goodsInfoETD.getZoneType();
        if (null == goodsInfoETD.getClassId()) {
            return ResponseUtils.errorRes("分类不能为空");
        }
        PGoodsClass pGoodsClass = classMapper.selectByPrimaryKey(goodsInfoETD.getClassId());
        if (null == pGoodsClass) {
            return ResponseUtils.errorRes("分类不存在");
        }
        if (StringUtil.isEmpty(goodsInfoETD.getGoodsName())) {
            return ResponseUtils.errorRes("产品名称不能为空");
        }
        PGoods goodsFind = goodsMapper.findByNameAndZoneType(goodsInfoETD.getGoodsName(),zoneType);
        if(null != goodsFind){
            return ResponseUtils.errorRes("同一专区有该名称产品存在");
        }
        if (null == goodsInfoETD.getRepertory() || goodsInfoETD.getRepertory().compareTo(new Integer(0)) <= 0) {
            return ResponseUtils.errorRes("库存错误");
        }
        if (null != goodsInfoETD.getPrice() && goodsInfoETD.getPrice().compareTo(new BigDecimal("0")) < 0) {
            return ResponseUtils.errorRes("商品价格错误");
        }
        Integer isSuit = goodsInfoETD.getIsSuit() == null ? 0 : goodsInfoETD.getIsSuit();
//        if(zoneType.equals(0)){
//            if(!isSuit.equals(0)){
//                return ResponseUtils.errorRes("普通专区只能是单品");
//            }
//        }
        if (!zoneType.equals(2)) {
            if (null == goodsInfoETD.getPrice() || goodsInfoETD.getPrice().compareTo(new BigDecimal("0")) <= 0) {
                return ResponseUtils.errorRes("商品价格必须大于0");
            }
        } else {
            if ((null == goodsInfoETD.getSixCouponNum() && null == goodsInfoETD.getThreeCouponNum()) ||
                    (goodsInfoETD.getSixCouponNum().compareTo(new Integer(0)) <= 0 && goodsInfoETD.getThreeCouponNum().compareTo(new Integer(0)) <= 0)) {
                return ResponseUtils.errorRes("请输入兑换券数量");
            }
        }
        if (StringUtil.isEmpty(goodsInfoETD.getSpecification())) {
            return ResponseUtils.errorRes("请输规格");
        }
        if (StringUtil.isEmpty(goodsInfoETD.getHostImg())) {
            return ResponseUtils.errorRes("请上传主图");
        }
        if (StringUtil.isEmpty(goodsInfoETD.getDetails())) {
            return ResponseUtils.errorRes("请输入产品详情");
        }
        if (StringUtil.isNotEmpty(goodsInfoETD.getTitle())) {
            goodsADD.setTitle(goodsInfoETD.getTitle());
        }
        Integer sort = goodsInfoETD.getSort() == null ? 0 : goodsInfoETD.getSort();
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
        if (0 == goodsMapper.insertSelective(goodsADD)) {
            throw new BusinessException("添加产品失败");
        }
        Long goodsId = goodsADD.getId();
        if (!this.addImgList(goodsInfoETD.getProductImgs(), goodsId)) {
            throw new BusinessException("添加产品图片失败");
        }
        if (!this.addGoodsSuit(goodsInfoETD,goodsId)) {
            throw new BusinessException("添加产品规格失败");
        }
        if (!this.addExchange(goodsInfoETD, goodsId)) {
            throw new BusinessException("添加兑换产品配置失败");
        }
        return ResponseUtils.successRes(1);
    }

    @Override
    @Transactional(rollbackFor = Exception.class, isolation = Isolation.READ_COMMITTED)
    public Response updateGoodsSortAndRepertory(GoodsInfoETD goodsInfoETD) {
        if (null == goodsInfoETD.getId()) {
            return ResponseUtils.errorRes("关键之错误");
        }
        PGoods goodsFind = goodsMapper.selectByPrimaryKey(goodsInfoETD.getId());
        if (null == goodsFind) {
            return ResponseUtils.errorRes("产品不存在");
        }
        PGoods goodsUPD = new PGoods();
        goodsUPD.setId(goodsFind.getId());
        if (null != goodsInfoETD.getSort()) {
            goodsUPD.setSort(goodsInfoETD.getSort());
        }
        if (null != goodsInfoETD.getRepertory() && goodsInfoETD.getRepertory().compareTo(new Integer(0)) > 0) {
            goodsUPD.setRepertory(goodsInfoETD.getRepertory());
        }
        if (0 == goodsMapper.updateByPrimaryKeySelective(goodsUPD)) {
            throw new BusinessException("修改失败");
        }
        return ResponseUtils.successRes(1);
    }

    @Override
    public Response updateGoodsIsDel(Long id) {
        try {
            isDelLock.lock();
            if (null == id) {
                return ResponseUtils.errorRes("关键之错误");
            }
            PGoods goodsFind = goodsMapper.selectByPrimaryKey(id);
            if (null == goodsFind) {
                return ResponseUtils.errorRes("产品不存在");
            }
            int isDel = 0;
            if (goodsFind.getIsDel().equals(new Integer(0))) {
                isDel = -1;
            }
            PGoods goodsUPD = new PGoods();
            goodsUPD.setId(goodsFind.getId());
            goodsUPD.setIsDel(isDel);
            if (0 == goodsMapper.updateByPrimaryKeySelective(goodsUPD)) {
                throw new BusinessException("失败");
            }
        } catch (Exception e) {
            throw new BusinessException(e.getMessage());
        } finally {
            isDelLock.unlock();
        }
        return ResponseUtils.successRes(1);
    }

    @Override
    public Response adminGoodsInfo(Long id) {
        GoodsInfoETD goodsInfo = goodsMapper.findAdminById(id);
        if(null == goodsInfo){
            return ResponseUtils.errorRes("产品不存在");
        }
        if(StringUtil.isNotEmpty(goodsInfo.getHostImg())){
            goodsInfo.setHostImg(imgPath+goodsInfo.getHostImg());
        }
        List<PGoodsImg> imgList = goodsImgMapper.selectListByGoodsId(id);
        if(null != imgList){
            for (PGoodsImg img:imgList){
                if(StringUtil.isNotEmpty(img.getImgPath())){
                    img.setImgPath(imgPath+img.getImgPath());
                }
            }
            goodsInfo.setImgList(imgList);
        }
        if(goodsInfo.getIsSuit().equals(new Integer(1))) {
            List<PGoodsSuit> suitList = goodsSuitMapper.selectAdminListByGoodsId(goodsInfo.getId());
            if (null != suitList && suitList.size() > 0) {
                goodsInfo.setSuitList(suitList);
            }
        }
        return ResponseUtils.successRes(goodsInfo);
    }

    @Override
    @Transactional(rollbackFor = Exception.class, isolation = Isolation.READ_COMMITTED)
    public Response goodsUpdate(GoodsInfoETD goodsInfoETD) {
        if(null == goodsInfoETD.getId()){
            return ResponseUtils.errorRes("关键值错误");
        }
        PGoods goodsFind = goodsMapper.selectByPrimaryKey(goodsInfoETD.getId());
        if(null == goodsFind){
            return ResponseUtils.errorRes("产品不存在");
        }
        PGoods goodsADD = new PGoods();
        goodsADD.setId(goodsFind.getId());
        Integer zoneType = goodsInfoETD.getZoneType() == null ? 0 : goodsInfoETD.getZoneType();
        if (null == goodsInfoETD.getClassId()) {
            return ResponseUtils.errorRes("分类不能为空");
        }
        PGoodsClass pGoodsClass = classMapper.selectByPrimaryKey(goodsInfoETD.getClassId());
        if (null == pGoodsClass) {
            return ResponseUtils.errorRes("分类不存在");
        }
        if (StringUtil.isEmpty(goodsInfoETD.getGoodsName())) {
            return ResponseUtils.errorRes("产品名称不能为空");
        }
        if (null == goodsInfoETD.getZoneType()) {
            return ResponseUtils.errorRes("请选择商品区域");
        }
        if (null == goodsInfoETD.getRepertory() || goodsInfoETD.getRepertory().compareTo(new Integer(0)) <= 0) {
            return ResponseUtils.errorRes("库存错误");
        }
        Integer isSuit = goodsInfoETD.getIsSuit() == null ? 0 : goodsInfoETD.getIsSuit();
        if (!zoneType.equals(2)) {
            if (null == goodsInfoETD.getPrice() || goodsInfoETD.getPrice().compareTo(new BigDecimal("0")) < 0) {
                return ResponseUtils.errorRes("商品价格有误");
            }
        } else {
            if ((null == goodsInfoETD.getSixCouponNum() && null == goodsInfoETD.getThreeCouponNum()) ||
                    (goodsInfoETD.getSixCouponNum().compareTo(new Integer(0)) <= 0 && goodsInfoETD.getThreeCouponNum().compareTo(new Integer(0)) <= 0)) {
                return ResponseUtils.errorRes("请输入兑换券数量");
            }
        }
        if (StringUtil.isEmpty(goodsInfoETD.getSpecification())) {
            return ResponseUtils.errorRes("请输规格");
        }
        if (StringUtil.isNotEmpty(goodsInfoETD.getHostImg())) {
            goodsADD.setHostImg(goodsInfoETD.getHostImg());
        }
        if (StringUtil.isEmpty(goodsInfoETD.getDetails())) {
            return ResponseUtils.errorRes("请输入产品详情");
        }
        if (StringUtil.isNotEmpty(goodsInfoETD.getTitle())) {
            goodsADD.setTitle(goodsInfoETD.getTitle());
        }
        Integer sort = goodsInfoETD.getSort() == null ? 0 : goodsInfoETD.getSort();

        goodsADD.setSort(sort);
        goodsADD.setIsSuit(isSuit);
        goodsADD.setCreateTime(new Date());
        goodsADD.setZoneType(zoneType);
        goodsADD.setGoodsName(goodsInfoETD.getGoodsName());
        goodsADD.setClassId(goodsInfoETD.getClassId());
        goodsADD.setPrice(goodsInfoETD.getPrice());
        goodsADD.setSpecification(goodsInfoETD.getSpecification());
        goodsADD.setDetails(goodsInfoETD.getDetails());
        goodsADD.setRepertory(goodsInfoETD.getRepertory());
        if (0 == goodsMapper.updateByPrimaryKeySelective(goodsADD)) {
            throw new BusinessException("修改产品失败");
        }
        Long goodsId = goodsFind.getId();
        if (!this.addImgList(goodsInfoETD.getProductImgs(), goodsId)) {
            throw new BusinessException("添加产品图片失败");
        }
        if (!this.addGoodsSuit(goodsInfoETD,goodsId)) {
            throw new BusinessException("添加产品规格失败");
        }
        if (!this.addExchange(goodsInfoETD, goodsId)) {
            throw new BusinessException("添加兑换产品配置失败");
        }
        return ResponseUtils.successRes(1);
    }

    private boolean addExchange(GoodsInfoETD goodsInfoETD, Long goodsId) {
        if (goodsInfoETD.getZoneType().equals(new Integer(1))) {
            exchangeMapper.deleteByGoodsId(goodsId);
        }else{
            if (null != goodsInfoETD.getSixCouponNum() && new Integer(0).compareTo(goodsInfoETD.getSixCouponNum()) < 0) {
                GoodsExchangeETD exchangeFind = exchangeMapper.selectByGoodsIdAndVoucherId(goodsId,1L);
                if(null == exchangeFind){
                    if (!this.insertExchange(goodsId, goodsInfoETD.getSixCouponNum(), 1L)) {
                        return false;
                    }
                }else{
                    PGoodsExchange exchangeUPD = new PGoodsExchange();
                    exchangeUPD.setId(exchangeFind.getId());
                    exchangeUPD.setNumber(goodsInfoETD.getSixCouponNum());
                    if(0 == exchangeMapper.updateByPrimaryKeySelective(exchangeUPD)){
                        return false;
                    }
                }

            }
            if (null != goodsInfoETD.getThreeCouponNum() && new Integer(0).compareTo(goodsInfoETD.getThreeCouponNum()) < 0) {
                GoodsExchangeETD exchangeFind = exchangeMapper.selectByGoodsIdAndVoucherId(goodsId,2L);
                if(null == exchangeFind) {
                    if (!this.insertExchange(goodsId, goodsInfoETD.getThreeCouponNum(), 2L)) {
                        return false;
                    }
                }else{
                    PGoodsExchange exchangeUPD = new PGoodsExchange();
                    exchangeUPD.setId(exchangeFind.getId());
                    exchangeUPD.setNumber(goodsInfoETD.getThreeCouponNum());
                    if(0 == exchangeMapper.updateByPrimaryKeySelective(exchangeUPD)){
                        return false;
                    }
                }
            }
        }
        return true;
    }

    private boolean insertExchange(Long goodsId, Integer number, Long vId) {
        PGoodsExchange exchangeADD = new PGoodsExchange();
        exchangeADD.setGoodsId(goodsId);
        exchangeADD.setVoucherId(vId);
        exchangeADD.setNumber(number);
        if (0 == exchangeMapper.insertSelective(exchangeADD)) {
            return false;
        }
        return true;
    }

    private boolean addGoodsSuit(GoodsInfoETD goodsInfoETD,Long goodsId) {
        if(null != goodsInfoETD.getIsSuit() && goodsInfoETD.getIsSuit().equals(new Integer(1))) {
            if (null != goodsInfoETD.getSuitList() && goodsInfoETD.getSuitList().size() > 0) {
                List<PGoodsSuit> suitList = goodsInfoETD.getSuitList();
                for (PGoodsSuit suit : suitList) {
                    if (StringUtil.isEmpty(suit.getExplain())) {
                        throw new BusinessException("规格配置不能为空");
                    }
                    PGoodsSuit goodsSuitADD = new PGoodsSuit();
                    goodsSuitADD.setExplain(suit.getExplain());
                    if (null == suit.getId()) {
                        goodsSuitADD.setGoodsId(goodsId);
                        goodsSuitADD.setCreateTime(new Date());
                        if (0 == goodsSuitMapper.insertSelective(goodsSuitADD)) {
                            return false;
                        }
                    } else {
                        goodsSuitADD.setId(suit.getId());
                        if (0 == goodsSuitMapper.updateByPrimaryKeySelective(goodsSuitADD)) {
                            return false;
                        }
                    }
                }
            }
        }else{
            goodsSuitMapper.deleteByGoodsId(goodsId);
        }
        return true;
    }

    private boolean addImgList(String productImgs, Long goodsId) {
        if (StringUtil.isNotEmpty(productImgs)) {
            String[] imgList = productImgs.split(",");
            if (imgList.length > 0) {
                for (String imgPath : imgList) {
                    String[] pathArr = imgPath.split("/");
                    String path = "";
                    if (pathArr.length > 0) {
                        path = pathArr[pathArr.length - 1];
                        PGoodsImg imgADD = new PGoodsImg();
                        imgADD.setImgPath(path);
                        imgADD.setGoodsId(goodsId);
                        if (0 == goodsImgMapper.insertSelective(imgADD)) {
                            return false;
                        }
                    }
                }
            }
        }
        return true;
    }
}
