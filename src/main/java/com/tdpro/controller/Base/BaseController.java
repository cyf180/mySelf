package com.tdpro.controller.Base;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageInfo;
import com.tdpro.common.constant.ErrorCodeConstants;
import com.tdpro.common.utils.QueryModel;
import com.tdpro.common.utils.Response;
import com.tdpro.common.utils.ResponseUtils;
import com.tdpro.service.Base.BaseService;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import springfox.documentation.annotations.ApiIgnore;

import javax.validation.Valid;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * Author: lijie
 * Email: admin@elevenstyle.com
 * Date: 2018/4/8
 * Time: 15:31
 */
public abstract class BaseController<E extends QueryModel> {


    protected Logger logger = LoggerFactory.getLogger(getClass());

    public abstract BaseService<E> getService();

    @PostMapping("/insert")
    @ApiOperation(value = "新增接口", response = Response.class)
    public Response insert(@ApiIgnore @RequestHeader("uId") Long uId,@ApiIgnore @RequestHeader("requestRealIp") String requestRealIp,
                           @Valid @RequestBody E e, BindingResult bindingResult) {
        Response response = ResponseUtils.handleValidError(bindingResult);
        if(response.getIsOK()) {
            e.setUId(uId);
            e.setIp(requestRealIp);
            StringBuffer sb = new StringBuffer();
            logger.info(sb.append("用户：").append(uId).append(" 执行新增操作 时间：").append(new Date()).toString());
            int a =getService().insertSelective(e);
            insertAfter(e);
            if(1!=a) {
                response = ResponseUtils.errorResReg(ErrorCodeConstants.ErrorCode.SYSTEM_INSERT_ERROR);
            } else {
                response.setData(e);
            }
        }
        return response;
    }

    @PostMapping("/update")
    @ApiOperation(value = "更新接口", response = Response.class)
    public Response update(@ApiIgnore @RequestHeader("uId") Long uId,@ApiIgnore @RequestHeader("requestRealIp") String requestRealIp,
                           @Valid @RequestBody E e, BindingResult bindingResult) {
        Response response = ResponseUtils.handleValidError(bindingResult);
        if(response.getIsOK()) {
            e.setUId(uId);
            e.setIp(requestRealIp);
            StringBuffer sb = new StringBuffer();
            logger.info(sb.append("用户：").append(uId).append(" 执行更新操作 时间：").append(new Date()).toString());

            int a = getService().updateByPrimaryKeySelective(e);
            insertAfter(e);
            if(1!=a) {
                response = ResponseUtils.errorResReg(ErrorCodeConstants.ErrorCode.SYSTEM_UPDATE_ERROR);
            } else {
                response.setData(e);
            }
        }
        return response;
    }

    @PostMapping("/delete")
    @ApiImplicitParam(name = "id", value = "记录id ",required = true,dataType = "long", paramType = "form")
    @ApiOperation(value = "删除接口", response = Response.class)
    public Response delete( @RequestBody JSONObject jsonObject) {
        Response response = ResponseUtils.successRes(null);
        Long id = jsonObject.getLong("id");
        if(null!=id) {
            StringBuffer sb = new StringBuffer();
            int a = getService().deleteByPrimaryKey(id);
            if(1!=a) {
                response = ResponseUtils.errorResReg(ErrorCodeConstants.ErrorCode.SYSTEM_DELETE_ERROR);
            } else {
                response.setData(a);
            }
        } else {
            response = ResponseUtils.errorRes();
        }
        return response;
    }

    @PostMapping("/remove")
    @ApiImplicitParam(name = "id", value = "记录id ",required = true,dataType = "long", paramType = "form")
    @ApiOperation(value = "删除并添加日志接口", response = Response.class)
    public Response delete( @RequestBody JSONObject jsonObject,@ApiIgnore @RequestHeader("uId") Long uId,@ApiIgnore @RequestHeader("requestRealIp") String requestRealIp) {
        Response response = ResponseUtils.successRes(null);
        Long id = jsonObject.getLong("id");
        if(null!=id) {
            StringBuffer sb = new StringBuffer();
            logger.info(sb.append("用户：").append(uId).append(" 执行删除操作 时间：").append(new Date()).toString());
            int a = getService().deleteByPrimaryKey(id,uId,requestRealIp);
            if(1!=a) {
                response = ResponseUtils.errorResReg(ErrorCodeConstants.ErrorCode.SYSTEM_DELETE_ERROR);
            } else {
                response.setData(a);
            }
        } else {
            response = ResponseUtils.errorRes();
        }
        return response;
    }


    @GetMapping("/selectOne")
    @ApiImplicitParam(name = "id", value = "记录id ",required = true,dataType = "long", paramType = "query")
    @ApiOperation(value = "单条查询接口", response = Response.class)
    public Response selectOne( Long id) {
        Response response = ResponseUtils.successRes(null);
        if(null!=id) {
            StringBuffer sb = new StringBuffer();
            //logger.info(sb.append("用户：").append(uid).append(" 执行单条查询操作 时间：").append(new Date()).toString());
            E e = getService().selectByPrimaryKey(id);
            response.setData(e);
        } else {
            response = ResponseUtils.errorResReg(ErrorCodeConstants.ErrorCode.SYSTEM_SELECT_ERROR);
        }
        return response;
    }

    @GetMapping("/selectList")
    @ApiOperation(value = "多条查询接口", response = Response.class)
    public Response selectList( E e) {
        //@ApiIgnore @RequestAttribute("uid") Integer uid
        StringBuffer sb = new StringBuffer();
        //logger.info(sb.append("用户：").append(uid).append(" 执行多条查询操作 时间：").append(new Date()).toString());
        Response response = ResponseUtils.successRes(null);
        Map<String,Object> result = new HashMap<String,Object>();
        PageInfo pageInfo = getService().selectPageList(e);
        result.put("pageInfo",pageInfo);
        result.put("queryModel",e);
        response.setData(result);
        return response;
    }

    protected void insertAfter(E e){
        e.clear();
    }
}
