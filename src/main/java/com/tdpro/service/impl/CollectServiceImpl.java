package com.tdpro.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.tdpro.common.exception.BusinessException;
import com.tdpro.common.utils.Response;
import com.tdpro.common.utils.ResponseUtils;
import com.tdpro.entity.PCollect;
import com.tdpro.entity.PGoods;
import com.tdpro.entity.extend.CollectETD;
import com.tdpro.entity.extend.GoodsETD;
import com.tdpro.mapper.PCollectMapper;
import com.tdpro.mapper.PGoodsMapper;
import com.tdpro.service.CollectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.sql.BatchUpdateException;
import java.util.Date;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

@Service
public class CollectServiceImpl implements CollectService {
    @Value("${qiniu.imgPath}")
    private String imgPath;
    @Autowired
    private PCollectMapper collectMapper;
    @Autowired
    private PGoodsMapper goodsMapper;

    Lock delLock = new ReentrantLock();

    @Override
    public Response userCollect(GoodsETD goodsETD,Long uid) {
        Integer pageNo = goodsETD.getPageNo() == null ? 1 : goodsETD.getPageNo();
        Integer pageSize = goodsETD.getPageSize() == null ? 10: goodsETD.getPageSize();
        PageHelper.startPage(pageNo,pageSize);
        List<GoodsETD> collectList = collectMapper.selectListByUid(uid);
        if(null != collectList && collectList.size() > 0){
            for (int i = 0;i<collectList.size();i++){
                if(!StringUtils.isEmpty(collectList.get(i).getHostImg())) {
                    collectList.get(i).setHostImg(imgPath + collectList.get(i).getHostImg());
                }
            }
        }
        return ResponseUtils.successRes(new PageInfo(collectList));
    }

    @Override
    public Response delCollect(CollectETD collectETD) {
        try {
            delLock.lock();
            if(null == collectETD.getId()){
                return ResponseUtils.errorRes("关键值错");
            }
            PCollect collect = collectMapper.selectByPrimaryKey(collectETD.getId());
            if(null == collect){
                return ResponseUtils.errorRes("收藏不存在");
            }
            if(!collect.getUid().equals(collectETD.getUid())){
                return ResponseUtils.errorRes("收藏数据异常");
            }
            if(0 == collectMapper.deleteByPrimaryKey(collect.getId())){
                throw new RuntimeException("删除失败");
            }
        }catch (Exception e){
            throw new RuntimeException(e.getMessage());
        }finally {
            delLock.unlock();
        }
        return ResponseUtils.successRes(1);
    }

    @Override
    public Response addCollect(PCollect collect){
        PGoods  goods = goodsMapper.selectByPrimaryKey(collect.getGoodsId());
        if(null == goods){
            return ResponseUtils.errorRes("产品不存在");
        }
        PCollect collectFind = collectMapper.findByUidAndGoodsId(collect.getUid(),collect.getGoodsId());
        if(null == collectFind){
            PCollect collectADD = new PCollect();
            collectADD.setUid(collect.getUid());
            collectADD.setGoodsId(collect.getGoodsId());
            collectADD.setCreateTime(new Date());
            if(0 == collectMapper.insertSelective(collectADD)){
                throw new BusinessException("收藏失败");
            }
        }else{
            if(0 == collectMapper.deleteByPrimaryKey(collectFind.getId())){
                throw new RuntimeException("删除失败");
            }
        }
        return ResponseUtils.successRes(1);
    }
}
