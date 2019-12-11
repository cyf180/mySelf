package com.tdpro.entity.extend;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

/**
 * @About
 * @Author jy
 * @Date 2018/7/2 15:24
 */
@Data
public class LoginETD {
    @ApiModelProperty(value = "账号")
    @NotBlank(message="帐号不能为空")
    private String userName;
    @ApiModelProperty(value = "密码")
    @NotBlank(message="密码不能为空")
    private String passWord;
}
