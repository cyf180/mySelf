package com.tdpro.controller;

import com.tdpro.common.utils.Response;
import com.tdpro.common.utils.ResponseUtils;
import com.tdpro.entity.PGoodsImg;
import com.tdpro.entity.PGoodsSuit;
import com.tdpro.entity.extend.GoodsInfoETD;
import com.tdpro.entity.extend.GoodsPageETD;
import com.tdpro.entity.extend.UserInfoETD;
import com.tdpro.service.GoodsImgService;
import com.tdpro.service.GoodsService;
import com.tdpro.service.GoodsSuitService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.validation.Valid;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

@RestController
@RequestMapping("/api/goods/")
@Api(tags = "运营端-产品相关")
public class AGoodsController {
    @Autowired
    private GoodsService goodsService;
    @Autowired
    private GoodsImgService imgService;
    @Autowired
    private GoodsSuitService suitService;
    Lock addLock = new ReentrantLock();
    Lock updSortLock = new ReentrantLock();
    @GetMapping("pageList")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageNo", value = "页码", required = true, dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "pageSize", value = "每页显示条数", required = true, dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "goodsName", value = "产品名称", required = false, dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = "classId", value = "分类Id", required = false, dataType = "long", paramType = "query")
    })
    @ApiOperation(value = "产品列表接口", response = GoodsPageETD.class)
    public Response getList(GoodsPageETD goodsPageETD) {
        return goodsService.adminPageList(goodsPageETD);
    }

    @GetMapping("goodsInfo")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "id", required = true, dataType = "int", paramType = "query")
    })
    @ApiOperation(value = "产品详情接口", response = GoodsInfoETD.class)
    public Response getGoodsInfo(Long id) {
        return goodsService.adminGoodsInfo(id);
    }

    @PostMapping("insertGoods")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "classId", value = "分类id", required = true, dataType = "long", paramType = "form"),
            @ApiImplicitParam(name = "goodsName", value = "产品名称", required = true, dataType = "string", paramType = "form"),
            @ApiImplicitParam(name = "zoneType", value = "专区类型(0:普通专区 1:会员专区 2:兑换专区)", required = true, dataType = "int", paramType = "form"),
            @ApiImplicitParam(name = "isSuit", value = "是否是套装(0:不是 1:是)", required = true, dataType = "int", paramType = "form"),
            @ApiImplicitParam(name = "price", value = "商品价格", required = true, dataType = "BigDecimal", paramType = "form"),
            @ApiImplicitParam(name = "title", value = "简介", required = true, dataType = "string", paramType = "form"),
            @ApiImplicitParam(name = "specification", value = "规格(单位)", required = true, dataType = "string", paramType = "form"),
            @ApiImplicitParam(name = "hostImg", value = "主图地址", required = true, dataType = "string", paramType = "form"),
            @ApiImplicitParam(name = "details", value = "商品详情", required = false, dataType = "string", paramType = "form"),
            @ApiImplicitParam(name = "sort", value = "排序", required = false, dataType = "int", paramType = "form"),
            @ApiImplicitParam(name = "explain", value = "规格", required = false, dataType = "string", paramType = "form"),
            @ApiImplicitParam(name = "productImgs", value = "详情图片", required = false, dataType = "string", paramType = "form"),
            @ApiImplicitParam(name = "sixCouponNum", value = "68元数量", required = false, dataType = "int", paramType = "form"),
            @ApiImplicitParam(name = "threeCouponNum", value = "34元数量", required = false, dataType = "int", paramType = "form"),
    })
    @ApiOperation(value = "添加产品")
    public Response insertGoods(@Valid @RequestBody GoodsInfoETD goodsInfoETD){
        Response response;
        try {
            addLock.lock();
            response = goodsService.addGoods(goodsInfoETD);
        }catch (Exception e){
            response = ResponseUtils.errorRes(e.getMessage());
        }finally {
            addLock.unlock();
        }
        return response;
    }

    @PostMapping("updGoods")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "id", required = true, dataType = "long", paramType = "form"),
            @ApiImplicitParam(name = "repertory", value = "库存", required = false, dataType = "int", paramType = "form"),
            @ApiImplicitParam(name = "sort", value = "排序", required = false, dataType = "int", paramType = "form"),
    })
    @ApiOperation(value = "修改库存和排序")
    public Response updateGoodsSort(@Valid @RequestBody GoodsInfoETD goodsInfoETD){
        Response response;
        try {
            updSortLock.lock();
            response = goodsService.updateGoodsSortAndRepertory(goodsInfoETD);
        }catch (Exception e){
            response = ResponseUtils.errorRes(e.getMessage());
        }finally {
            updSortLock.unlock();
        }
        return response;
    }

    @PostMapping("goodsUpDown")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "产品id", required = true, dataType = "long", paramType = "form"),
    })
    @ApiOperation(value = "产品上下架")
    public Response goodsUpDown(@Valid @RequestBody GoodsInfoETD goodsInfoETD){
        return goodsService.updateGoodsIsDel(goodsInfoETD.getId());
    }

    @PostMapping("update")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "id", required = true, dataType = "long", paramType = "form"),
            @ApiImplicitParam(name = "classId", value = "分类id", required = true, dataType = "long", paramType = "form"),
            @ApiImplicitParam(name = "goodsName", value = "产品名称", required = true, dataType = "string", paramType = "form"),
            @ApiImplicitParam(name = "zoneType", value = "专区类型(0:普通专区 1:会员专区 2:兑换专区)", required = true, dataType = "int", paramType = "form"),
            @ApiImplicitParam(name = "isSuit", value = "是否是套装(0:不是 1:是)", required = true, dataType = "int", paramType = "form"),
            @ApiImplicitParam(name = "price", value = "商品价格", required = true, dataType = "BigDecimal", paramType = "form"),
            @ApiImplicitParam(name = "title", value = "简介", required = true, dataType = "string", paramType = "form"),
            @ApiImplicitParam(name = "specification", value = "规格(单位)", required = true, dataType = "string", paramType = "form"),
            @ApiImplicitParam(name = "hostImg", value = "主图地址", required = true, dataType = "string", paramType = "form"),
            @ApiImplicitParam(name = "details", value = "商品详情", required = false, dataType = "string", paramType = "form"),
            @ApiImplicitParam(name = "sort", value = "排序", required = false, dataType = "int", paramType = "form"),
            @ApiImplicitParam(name = "explain", value = "规格", required = false, dataType = "string", paramType = "form"),
            @ApiImplicitParam(name = "productImgs", value = "详情图片", required = false, dataType = "string", paramType = "form"),
            @ApiImplicitParam(name = "sixCouponNum", value = "68元数量", required = false, dataType = "int", paramType = "form"),
            @ApiImplicitParam(name = "threeCouponNum", value = "34元数量", required = false, dataType = "int", paramType = "form"),
    })
    @ApiOperation(value = "修改产品")
    public Response updateGoods(@Valid @RequestBody GoodsInfoETD goodsInfoETD){
        Response response;
        try {
            updSortLock.lock();
            response = goodsService.goodsUpdate(goodsInfoETD);
        }catch (Exception e){
            response = ResponseUtils.errorRes(e.getMessage());
        }finally {
            updSortLock.unlock();
        }
        return response;
    }

    @PostMapping("imgDel")
    @ApiOperation(value = "产品图片删除")
    public Response imgDel(@Valid @RequestBody PGoodsImg goodsImg){
        Response response;
        if(!imgService.delById(goodsImg.getId())){
            response = ResponseUtils.errorRes("删除失败");
        }else{
            response = ResponseUtils.successRes(1);
        }
        return response;
    }

    @PostMapping("suitDel")
    @ApiOperation(value = "产品规格删除")
    public Response imgSuit(@Valid @RequestBody PGoodsSuit suit){
        Response response;
        if(!suitService.delSuitById(suit.getId())){
            response = ResponseUtils.errorRes("删除失败");
        }else{
            response = ResponseUtils.successRes(1);
        }
        return response;
    }
}
