package com.github.pampas.ui.vo.req;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.github.pampas.ui.base.vo.Request;
import io.swagger.annotations.ApiModel;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * Description:
 * User: darrenfu
 * Date: 2018-12-05
 */
@Data
@ApiModel(value = "根据ID操作请求")
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class IdOperationReq implements Request {
    @NotNull(message = "ID不能为空")
    private Integer id;
}
