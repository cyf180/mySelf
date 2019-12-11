package com.tdpro.entity.extend;


import com.tdpro.common.utils.QueryModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

@Data
public class RoleETD extends QueryModel {
    private static final long serialVersionUID = 1698077231323249163L;
    private Long id;
    @NotEmpty(message = "角色名称不能为空")
    @ApiModelProperty(value = "角色名称")
    private String roleRank;
    @NotEmpty(message = "角色标识不能为空")
    @ApiModelProperty(value = "角色标识")
    private String roleName;
    @ApiModelProperty(value = "添加时间")
    private Date roleTime;
    @NotNull(message = "角色状态不能为空")
    @ApiModelProperty(value = "角色状态")
    private Integer roleStatus;
    @ApiModelProperty(value = "角色状态类型默认为空")
    private String roleType;
    @ApiModelProperty(value = "拥有的菜单id ','拼接")
    private String privileges;
    @NotNull
    @ApiModelProperty(value = "1:新增 2:更新 ")
    private Integer insertOrUpdate;
    @Override
    public void clear() {
        super.clear();
        id=null;
        roleRank=null;
        roleName=null;
        roleTime=null;
        roleStatus=null;
        privileges=null;
        insertOrUpdate=null;
    }
}