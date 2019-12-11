package com.tdpro.entity.extend;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Map;

/**
 * @About 登录结果
 * @Author jy
 * @Date 2018/7/2 15:41
 */
@Data
public class LoginResultETD {
    @ApiModelProperty(value = "账号")
    String name;

    @ApiModelProperty(value = "token")
    String token;

    @ApiModelProperty(value = "菜单信息")
    Map<String,Object> menus;

}
