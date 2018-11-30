package com.github.pampas.ui.vo.resp;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.github.pampas.storage.entity.ServiceInstance;
import com.github.pampas.ui.vo.req.InstanceSaveReq;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * Description:
 * User: darrenfu
 * Date: 2018-11-20
 */
@Data
@ApiModel("查询实例信息响应")
@EqualsAndHashCode(callSuper = false)
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class InstanceResp extends ServiceInstance {

    private List<InstanceSaveReq.KeyAndVal> propList;

}
