package com.github.pampas.ui.vo.req;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.github.pampas.ui.base.vo.Request;
import io.swagger.annotations.ApiModel;
import lombok.Data;

/**
 * Description:
 * User: darrenfu
 * Date: 2018-11-28
 */
@Data
@ApiModel(value = "查询服务路由规则请求")
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class RouteRuleListReq implements Request {

    private String gatewayInstanceId;

    private String gatewayGroup;
}
