package com.github.pampas.ui.vo.req;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.github.pampas.ui.base.vo.Request;
import io.swagger.annotations.ApiModel;
import lombok.Data;

/**
 * Description:
 * User: darrenfu
 * Date: 2018-11-16
 */
@Data
@ApiModel(value = "查询服务列表请求")
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class ServiceListReq implements Request {

    private String group;
    private String serviceName;

}
