package com.github.pampas.ui.vo.req;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.github.pampas.ui.base.RegistryTypeEnum;
import com.github.pampas.ui.base.vo.Request;
import io.swagger.annotations.ApiModel;
import lombok.Data;

/**
 * Description:
 * User: darrenfu
 * Date: 2018-12-04
 */
@Data
@ApiModel(value = "保存注册中心请求")
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class ServiceRegistrySaveReq implements Request {

    private Integer id;

    private String name;

    private RegistryTypeEnum type;

    private String address;


    private String remark;
}
