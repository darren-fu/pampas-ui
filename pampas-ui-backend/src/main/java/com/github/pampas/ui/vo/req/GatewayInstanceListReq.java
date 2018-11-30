package com.github.pampas.ui.vo.req;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.github.pampas.ui.base.vo.Request;
import io.swagger.annotations.ApiModel;
import lombok.Data;

/**
 * Description:
 * User: darrenfu
 * Date: 2018-11-30
 */
@Data
@ApiModel(value = "查询网关实例列表请求")
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class GatewayInstanceListReq implements Request {

    private String instanceId;

    private String group;

    private String name;

    private String host;

    private String hostName;
}
