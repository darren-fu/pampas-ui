package com.github.pampas.ui.vo.req;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.github.pampas.ui.base.RegistryPatternEnum;
import com.github.pampas.ui.base.RegistryTypeEnum;
import com.github.pampas.ui.base.vo.Request;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;

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

    @NotEmpty(message = "名称不能为空")
    private String name;

    @NotNull(message = "类型不能为空")
    private RegistryTypeEnum type;

    @NotNull(message = "模式不能为空")
    private RegistryPatternEnum pattern;

    @NotEmpty(message = "地址不能为空")
    private String address;


    private String remark;
}
