package com.tdpro.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.github.pagehelper.StringUtil;
import com.tdpro.common.exception.BusinessException;
import com.tdpro.common.utils.Response;
import com.tdpro.common.utils.ResponseUtils;
import com.tdpro.entity.PGoods;
import com.tdpro.entity.PGoodsClass;
import com.tdpro.entity.extend.GoodsClassPageETD;
import com.tdpro.mapper.PGoodsClassMapper;
import com.tdpro.service.GoodsClassService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

@Service
public class GoodsClassServiceImpl implements GoodsClassService {
    @Value("${qiniu.imgPath}")
    private String imgPath;
    @Autowired
    private PGoodsClassMapper goodsClassMapper;

    Lock isShowLock = new ReentrantLock();

    Lock updLock = new ReentrantLock();

    @Override
    public List<PGoodsClass> getList(Integer num) {
        List<PGoodsClass> classList = goodsClassMapper.selectList(num);
        if (null != classList && classList.size() > 0) {
            for (PGoodsClass goodsClass : classList) {
                if (StringUtil.isNotEmpty(goodsClass.getIconPath())) {
                    goodsClass.setIconPath(imgPath + goodsClass.getIconPath());
                }
            }
        }
        return classList;
    }

    @Override
    public Response adminClassAllList() {
        GoodsClassPageETD classPageETD = new GoodsClassPageETD();
        List<GoodsClassPageETD> list = goodsClassMapper.findGoodsClassPageList(classPageETD);
        return ResponseUtils.successRes(list);
    }

    @Override
    public Response adminGoodsClassPageList(GoodsClassPageETD classPageETD) {
        Integer pageNum = classPageETD.getPageNo() == null ? 1 : classPageETD.getPageNo();
        Integer pageSize = classPageETD.getPageSize() == null ? 10 : classPageETD.getPageSize();
        PageHelper.startPage(pageNum, pageSize);
        List<GoodsClassPageETD> list = goodsClassMapper.findGoodsClassPageList(classPageETD);
        if (null != list && list.size() > 0) {
            for (GoodsClassPageETD classPage : list) {
                if (StringUtil.isNotEmpty(classPage.getIconPath())) {
                    classPage.setIconPath(imgPath + classPage.getIconPath());
                }
            }
        }
        Map map = new HashMap();
        map.put("pageInfo",new PageInfo(list));
        map.put("queryModel",classPageETD);
        return ResponseUtils.successRes(map);
    }

    @Override
    public Response addOrUpdateClass(PGoodsClass goodsClass){
        try {
            updLock.lock();
            if(null == goodsClass.getId()){
                if(StringUtil.isEmpty(goodsClass.getClassName())){
                    return ResponseUtils.errorRes("分类名错误");
                }
                PGoodsClass goodsClassFind = goodsClassMapper.findByNameOrNotId(goodsClass);
                if(null != goodsClassFind){
                    return ResponseUtils.errorRes("分类名已存在");
                }
                if(StringUtil.isEmpty(goodsClass.getIconPath())){
                    return ResponseUtils.errorRes("图标上传错误");
                }
                PGoodsClass classADD = new PGoodsClass();
                classADD.setClassName(goodsClass.getClassName());
                classADD.setIconPath(goodsClass.getIconPath());
                Integer sort = goodsClass.getSort() == null ? 0 : goodsClass.getSort();
                classADD.setSort(sort);
                Integer isShow = goodsClass.getIsShow() == null ? 0 : goodsClass.getIsShow();
                classADD.setIsShow(isShow);
                if(StringUtil.isNotEmpty(goodsClass.getExplain())){
                    classADD.setExplain(goodsClass.getExplain());
                }
                classADD.setCreateTime(new Date());
                if(0==goodsClassMapper.insertSelective(classADD)){
                    throw new BusinessException("添加失败");
                }
            }else{
                PGoodsClass classFind = goodsClassMapper.selectByPrimaryKey(goodsClass.getId());
                if (null == classFind) {
                    return ResponseUtils.errorRes("分类不存在");
                }
                if(StringUtil.isNotEmpty(goodsClass.getClassName())) {
                    PGoodsClass goodsClassFind = goodsClassMapper.findByNameOrNotId(goodsClass);
                    if(null != goodsClassFind){
                        return ResponseUtils.errorRes("分类名已存在");
                    }
                }
                if(0 == goodsClassMapper.updateByPrimaryKeySelective(goodsClass)){
                    throw new BusinessException("修改失败");
                }
            }
        }catch (Exception e){
            throw new BusinessException(e.getMessage());
        }finally {
            updLock.unlock();
        }
        return ResponseUtils.successRes(1);
    }

    @Override
    public Response updateIsShow(PGoodsClass goodsClass){
        try {
            isShowLock.lock();
            if(null == goodsClass.getId()){
                return ResponseUtils.errorRes("关键值错误");
            }
            PGoodsClass classFind = goodsClassMapper.selectByPrimaryKey(goodsClass.getId());
            if(null == classFind){
                return ResponseUtils.errorRes("分类不存在");
            }
            int isShow = 0;
            if(classFind.getIsShow().equals(new Integer(0))){
                isShow = 1;
            }
            PGoodsClass classUPD = new PGoodsClass();
            classUPD.setId(classFind.getId());
            classUPD.setIsShow(isShow);
            if(0 == goodsClassMapper.updateByPrimaryKeySelective(classUPD)){
                throw new BusinessException("修改失败");
            }
        }catch (Exception e){
            throw new BusinessException(e.getMessage());
        }finally {
            isShowLock.unlock();
        }
        return ResponseUtils.successRes(1);
    }

    @Override
    public  Response classInfo(Long id){
        PGoodsClass classInfo = goodsClassMapper.selectByPrimaryKey(id);
        if(null !=classInfo && StringUtil.isNotEmpty(classInfo.getIconPath())){
            classInfo.setIconPath(imgPath+classInfo.getIconPath());
        }
        return ResponseUtils.successRes(classInfo);
    }
}
