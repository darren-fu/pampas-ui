package com.github.pampas.ui.vo.resp;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.github.pampas.ui.base.RegistryTypeEnum;
import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.util.Date;

/**
 * Description:
 * User: darrenfu
 * Date: 2018-12-04
 */
@Data
@ApiModel(value = "注册中心响应")
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class ServiceRegistryResp {

    private Integer id;

    private String name;

    private String type;

    private String address;


    private String remark;

    private Date updateTime;
}
