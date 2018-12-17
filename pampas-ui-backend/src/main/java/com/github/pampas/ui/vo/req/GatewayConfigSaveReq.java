package com.github.pampas.ui.vo.req;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.github.pampas.ui.base.vo.Request;
import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.util.List;

/**
 * Description:
 * User: darrenfu
 * Date: 2018-12-17
 */
@Data
@ApiModel(value = "保存网关Config请求")
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class GatewayConfigSaveReq implements Request {

    private String gatewayGroup;
    private String gatewayInstanceId;
    private String configSpiClass;

    private List<KeyAndVal> list;


    @Data
    @ApiModel(value = "键值对")
    @JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
    public static class KeyAndVal{
        private Integer id;

        private String key;

        private String value;

        private boolean required;
    }


}
