package com.tdpro.controller;

import com.tdpro.common.utils.Response;
import com.tdpro.entity.extend.GoodsClassPageETD;
import com.tdpro.entity.extend.OrderPageETD;
import com.tdpro.entity.extend.VoucherPageETD;
import com.tdpro.service.VoucherService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/voucher/")
@Api(tags = "运营端-优惠券")
public class AVoucherController {
    @Autowired
    private VoucherService voucherService;
    @GetMapping("pageList")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageNo", value = "页码", required = true, dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "pageSize", value = "每页显示条数", required = true, dataType = "int", paramType = "query")
    })
    @ApiOperation(value = "产品分类列表接口", response = VoucherPageETD.class)
    public Response getOrderList(VoucherPageETD voucherPageETD) {
        return voucherService.adminPageList(voucherPageETD);
    }
}
