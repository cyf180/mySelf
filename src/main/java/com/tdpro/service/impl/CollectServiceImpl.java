package com.tdpro.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.tdpro.common.exception.BusinessException;
import com.tdpro.common.utils.Response;
import com.tdpro.common.utils.ResponseUtils;
import com.tdpro.entity.PCollect;
import com.tdpro.entity.extend.CollectETD;
import com.tdpro.mapper.PCollectMapper;
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

    Lock delLock = new ReentrantLock();

    @Override
    public Response userCollect(CollectETD collectETD) {
        Integer pageNo = collectETD.getPageNo() == null ? 1 : collectETD.getPageNo();
        Integer pageSize = collectETD.getPageSize() == null ? 10: collectETD.getPageSize();
        PageHelper.startPage(pageNo,pageSize);
        List<CollectETD> collectList = collectMapper.selectListByUid(collectETD.getUid());
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
        Response response = ResponseUtils.successRes(1);
        try {
            delLock.lock();
            if(null == collectETD.getId()){
                response = ResponseUtils.errorRes("关键值错");
            }
            PCollect collect = collectMapper.selectByPrimaryKey(collectETD.getId());
            if(null == collect){
                response = ResponseUtils.errorRes("收藏不存在");
            }
            if(!collect.getUid().equals(collectETD.getUid())){
                response = ResponseUtils.errorRes("收藏数据异常");
            }
            if(0 == collectMapper.deleteByPrimaryKey(collect.getId())){
                throw new RuntimeException("删除失败");
            }
        }catch (Exception e){
            throw new RuntimeException(e.getMessage());
        }finally {
            delLock.unlock();
        }
        return response;
    }

    @Override
    public Response addCollect(PCollect collect){
        PCollect collectFind = collectMapper.findByUidAndGoodsId(collect.getUid(),collect.getGoodsId());
        if(null != collectFind){
            PCollect collectADD = new PCollect();
            collect.setUid(collect.getUid());
            collectADD.setGoodsId(collect.getGoodsId());
            collectADD.setCreateTime(new Date());
            if(0 == collectMapper.insertSelective(collectADD)){
                throw new BusinessException("收藏失败");
            }
        }else{
            if(0 == collectMapper.deleteByPrimaryKey(collect.getId())){
                throw new RuntimeException("删除失败");
            }
        }
        return ResponseUtils.successRes(1);
    }
}
