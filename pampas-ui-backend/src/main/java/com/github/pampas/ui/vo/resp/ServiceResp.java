package com.github.pampas.ui.vo.resp;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.validator.constraints.NotEmpty;

import java.util.Date;

/**
 * Description:
 * User: darrenfu
 * Date: 2018-11-16
 */
@Data
@ApiModel("查询Service响应")
@EqualsAndHashCode(callSuper = false)
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class ServiceResp {

    private Integer id;

    private String group;

    private String serviceName;

    // sc dubbo grpc
    @NotEmpty(message = "服务类型不能为空")
    private String type;

    // http
    private String protocol;

    //注册中心
    private Integer registryId;

    private String registryName;

    private String remark;

    private Boolean status;

    private String loadbalancer;
    private String loadbalancerName;

    private Date insertTime;

    private Date updateTime;

}
