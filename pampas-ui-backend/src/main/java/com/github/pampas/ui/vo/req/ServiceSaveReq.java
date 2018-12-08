package com.github.pampas.ui.vo.req;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.github.pampas.ui.base.ServiceTypeEnum;
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
@ApiModel(value = "保存服务请求")
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class ServiceSaveReq implements Request {

    private Integer id;

    @NotEmpty(message = "服务名称不能为空")
    private String serviceName;

    // sc dubbo grpc
    @NotNull(message = "服务类型不能为空")
    private ServiceTypeEnum type;

    // http
    private String protocol;

    //注册中心
    private Integer registryId;

    private String group;

    @NotNull(message = "状态不能为空")
    private Boolean status;

    private String remark;

}
