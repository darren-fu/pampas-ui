package com.github.pampas.ui.vo.req;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.github.pampas.ui.base.vo.Request;
import io.swagger.annotations.ApiModel;
import lombok.Data;

/**
 * Description:
 * User: darrenfu
 * Date: 2018-10-06
 */
@Data
@ApiModel(value = "用户登录请求")
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class UserLoginReq implements Request {
    private String userName;
    private String password;
}
