package com.github.pampas.ui.vo.resp;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.github.pampas.storage.entity.GatewayConfig;
import com.github.pampas.storage.entity.GatewaySpi;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Description:
 * User: darrenfu
 * Date: 2018-12-10
 */
@Data
@ApiModel("查询网关SPI信息响应")
@EqualsAndHashCode(callSuper = false)
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class GatewaySpiResp extends GatewaySpi {
    private Integer rowSpan;

}
