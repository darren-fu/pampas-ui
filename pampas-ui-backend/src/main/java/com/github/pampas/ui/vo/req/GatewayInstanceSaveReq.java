package com.github.pampas.ui.vo.req;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.github.pampas.storage.entity.GatewayInstance;
import com.github.pampas.ui.base.vo.Request;
import io.swagger.annotations.ApiModel;
import lombok.Data;

/**
 * Description:
 * User: darrenfu
 * Date: 2018-11-30
 */
@Data
@ApiModel(value = "保存网关实例请求")
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class GatewayInstanceSaveReq extends GatewayInstance implements Request {


}
