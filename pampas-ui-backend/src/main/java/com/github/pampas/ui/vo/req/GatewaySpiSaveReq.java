package com.github.pampas.ui.vo.req;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.github.pampas.ui.base.vo.Request;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * Description:
 * User: darrenfu
 * Date: 2018-12-17
 */
@Data
@ApiModel(value = "保存网关Spi请求")
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class GatewaySpiSaveReq implements Request {

    private String gatewayGroup;
    private String gatewayInstanceId;
    private String spiInterface;
    @NotEmpty(message = "插件列表不能为空")
    private List<KeyAndVal> list;


    @Data
    @ApiModel(value = "键值对")
    @JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
    public static class KeyAndVal {

        @NotNull(message = "id不能为空")
        private Integer id;

        private Integer order;

        private Boolean status;
    }


}
