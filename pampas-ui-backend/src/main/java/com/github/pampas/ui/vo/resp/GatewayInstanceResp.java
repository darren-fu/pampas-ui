package com.github.pampas.ui.vo.resp;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.github.pampas.storage.entity.GatewayInstance;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Description:
 * User: darrenfu
 * Date: 2018-11-28
 */
@Data
@ApiModel("查询网关实例信息响应")
@EqualsAndHashCode(callSuper = false)
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class GatewayInstanceResp extends GatewayInstance {


    private Integer relRuleNum;
}
