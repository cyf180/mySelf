package com.tdpro.controller;

import com.tdpro.common.utils.Response;
import com.tdpro.common.utils.ResponseUtils;
import com.tdpro.entity.PAdvert;
import com.tdpro.entity.POrder;
import com.tdpro.entity.extend.AdvertETD;
import com.tdpro.entity.extend.GoodsClassPageETD;
import com.tdpro.entity.extend.OrderPageETD;
import com.tdpro.service.AdvertService;
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
@RequestMapping("/api/advert/")
@Api(tags = "运营端-广告管理")
public class AAdvertController {
    @Autowired
    private AdvertService advertService;

    Lock updLock = new ReentrantLock();

    Lock delLock = new ReentrantLock();

    @GetMapping("pageList")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageNo", value = "页码", required = true, dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "pageSize", value = "每页显示条数", required = true, dataType = "int", paramType = "query")
    })
    @ApiOperation(value = "公告列表", response = AdvertETD.class)
    public Response getAdvertList(AdvertETD advertETD) {
        return advertService.adminPageList(advertETD);
    }

    @GetMapping("advertInfo")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "页码", required = true, dataType = "int", paramType = "query")
    })
    @ApiOperation(value = "公告详情", response = PAdvert.class)
    public Response getAdvertInfo(Long id) {
        return advertService.adminAdvertInfo(id);
    }

    @PostMapping("addOrUpdate")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "订单id", required = true, dataType = "long", paramType = "form")
    })
    @ApiOperation(value = "公告修改")
    public Response getAddOrUpdate(@ApiIgnore @RequestAttribute("adminId") Long adminId, @Valid @RequestBody PAdvert advert){
        Response response;
        try {
            updLock.lock();
            response = advertService.updateAdvert(advert);
        }catch (Exception e){
            response = ResponseUtils.errorRes(e.getMessage());
        }finally {
            updLock.unlock();
        }
        return response;
    }

    @PostMapping("del")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "订单id", required = true, dataType = "long", paramType = "form")
    })
    @ApiOperation(value = "删除修改")
    public Response delAdvert(@ApiIgnore @RequestAttribute("adminId") Long adminId, @Valid @RequestBody PAdvert advert){
        Response response;
        try {
            delLock.lock();
            response = advertService.deleteById(advert.getId());
        }catch (Exception e){
            response = ResponseUtils.errorRes(e.getMessage());
        }finally {
            delLock.unlock();
        }
        return response;
    }

}
