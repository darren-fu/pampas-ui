package com.github.pampas.ui.vo.req;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.github.pampas.ui.base.vo.Request;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;

/**
 * Description:
 * User: darrenfu
 * Date: 2018-11-16
 */
@Data
@ApiModel(value = "保存服务实例请求")
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class InstanceSaveReq implements Request {

    private Integer id;

    private String instanceId;

    @NotNull(message = "服务ID不能为空")
    private Integer serviceId;

    @NotEmpty(message = "服务名称不能为空")
    private String serviceName;

    @NotEmpty(message = "服务地址不能为空")
    private String host;

    private String hostName;

    private String room;

    @NotNull(message = "服务端口不能为空")
    private Integer port;

    private Integer weight;

    private Byte status;

    private String group;

    private String remark;

}
