package com.tdpro.entity.extend;



import com.tdpro.common.utils.QueryModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class MenuETD extends QueryModel {
    private Long id;
    @ApiModelProperty(value = "所属父级菜单id")
    private Long mid;
    @ApiModelProperty(value = "菜单")
    private String menuName;
    @ApiModelProperty(value = "菜单路径")
    private String menuUrl;
    @ApiModelProperty(value = "菜单类型button：按钮,page:页面,module:模块")
    private String menuType;
    @ApiModelProperty(value = "接口地址")
    private String menuApiPath;

}